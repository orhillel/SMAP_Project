var express = require('express');
var app = express();
var path = require('path');
var fs = require('fs');
var port = process.env.PORT || 8000

app.use('/Scripts',express.static(__dirname + '/assets/Javascripts'));
app.use('/Images',express.static(__dirname + '/assets/Images'));

app.get('/', function(req,res){
    res.sendFile('FrontPage.html', {root: path.join(__dirname, './html')})
});

app.get(/^(.(.*\.html))*$/, function(req,res){
    res.sendFile(req.params[0], {root: path.join(__dirname, './html')})
});

app.listen(port, function(){
    console.log('Server startes')
});
