var express = require('express');
var path = require('path');
var favicon = require('serve-favicon');
var logger = require('morgan');
var cookieParser = require('cookie-parser');
var bodyParser = require('body-parser');
var mysql= require('mysql');
var connection=mysql.createConnection({
    host: "127.0.0.1",
    user: 'root',
    password: 'sp123456',

    database: 'myblogtest'
});
connection.connect(function(err) {
    if (err) {
        console.error('error connecting: ' + err.stack);
        return;
    }
    console.log('connected as id ' + connection.threadId);
});


var insertToDb=function (uuid, usetimes, date) {
    var content=[uuid,usetimes,date];
    connection.query('INSERT INTO userecord (uuid,usetimes,date) VALUES(?,?,?)',content);
};
var queryFromDbToInsert=function (uuid,date,usetimes) {
    var content=[uuid,date];
    connection.query('SELECT * from userecord where uuid=? and date=?',content,function (error, results, fields) {
        if (error) throw error;
        if(results.length>0){
            if (usetimes>(results[0].usetimes)){
                var content=[usetimes,uuid,date];
                connection.query( 'UPDATE userecord SET usetimes = ? WHERE uuid = ? and date = ?',content,function (err, result) {
                    if (err) {
                        console.log('[UPDATE ERROR] - ', err.message);
                        return;
                    }
                    console.log('----------UPDATE-------------');
                    console.log('UPDATE affectedRows', result.affectedRows);
                    console.log('******************************');
                });
            }
        }else {
            insertToDb(uuid, usetimes, date);
        }
    });
};
var queryFromDb=function (uuid, date, usetimes,socket) {
    var content=[uuid,date,usetimes];
    connection.query('SELECT * from userecord where uuid=? and date=?',content,function (error, results, fields) {
        if (error) throw error;
        if (results.length>0){
            var serverUseTimes=results[0].usetimes;
            if (serverUseTimes>usetimes){
                console.log("准备同步:",serverUseTimes);
                socket.emit("synchronizeToClient",serverUseTimes);
            }
        }
    });
}
//connection.end();

var app = express();
var server=require('http').createServer(app);
var io = require('socket.io').listen(server);
server.listen(process.env.PORT || 4000);
// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'ejs');//指定视图模板引擎

// uncomment after placing your favicon in /public
//app.use(favicon(path.join(__dirname, 'public', 'favicon.ico')));
app.use(logger('dev'));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

//路由规则
var index = require('./routes/index');
var users = require('./routes/users');

app.use('/users', users);
app.use('/', index);

// catch 404 and forward to error handler
app.use(function(req, res, next) {
  var err = new Error('Not Found');
  err.status = 404;
  next(err);
});
// error handler
app.use(function(err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get('env') === 'development' ? err : {};

  // render the error page
  res.status(err.status || 500);
  res.render('error');
});

//监听socket
io.sockets.on('connection',function (socket) {
    console.log("new connection");
    //客户端向服务器同步
    socket.on('updateInDb',function (uuid, usetimes, date) {
        console.log("insert:",uuid,usetimes,date);
        queryFromDbToInsert(uuid,date,usetimes);
    });

    //服务器向客户端同步
    socket.on('queryFromServer',function (uuid,usetimes,date) {
        console.log("syncronizeToClient");
        queryFromDb(uuid,date,usetimes,socket);
    })

});

module.exports = app;
