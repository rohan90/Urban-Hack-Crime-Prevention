var mongoose = require('mongoose')
    , fs = require('fs')
    , models_path = process.cwd() + '/app/models'

mongoose.connect("mongodb://localhost/uh");
/*mongoose.connect(process.env.DATABASE_URI, function (err) {
  if (err) {
    console.log(err);
  }
});*/
// mongoose.connect('mongodb://heroku_zgztp667:4ii90fq78ie4mcfvccafcfa8ad@ds027744.mongolab.com:27744/heroku_zgztp667');
var db = mongoose.connection;

db.on('error', function (err) {
    console.error('MongoDB connection error:', err);
});
db.once('open', function callback() {
    console.info('MongoDB connection is established');
});
db.on('disconnected', function() {
    console.error('MongoDB disconnected!');
    mongoose.connect(process.env.MONGO_URL, {server:{auto_reconnect:true}});
});
db.on('reconnected', function () {
    console.info('MongoDB reconnected!');
});

fs.readdirSync(models_path).forEach(function (file) {
    if (~file.indexOf('.js'))
        require(models_path + '/' + file)
});