


var user = {
    insert:'INSERT INTO userecord(uuid,usetimes,date) VALUES(0,?,?)',
    update:'update user set name=?, age=? where id=?',
    delete: 'delete from user where id=?',
    queryByUUID: 'select * from user where id=?',
    queryAll: 'select * from user'
};
module.exports = user;