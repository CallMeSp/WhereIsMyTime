var express = require('express');
var router = express.Router();

/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('index', { title: 'Express' });
});

router.get('/shit',function (req,res) {
    res.send('fuck this code one by one');
})

module.exports = router;
