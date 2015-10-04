var restify = require('restify')
    , fs = require('fs')


var controllers = {}
    , controllers_path = process.cwd() + '/app/controllers'
fs.readdirSync(controllers_path).forEach(function (file) {
    if (file.indexOf('.js') != -1) {
        controllers[file.split('.')[0]] = require(controllers_path + '/' + file)
    }
})

var server = restify.createServer();

server
    .use(restify.fullResponse())
    .use(restify.bodyParser())

//healthcheck
server.get("/healthcheck", healthcheck)

function healthcheck(req,res){
    res.writeHead(200, {'Content-Type': 'text/plain'});
    console.log("received a healthcheck hit\n")
    res.end('Server is up..\n');
} 

var base = "/api/v1"


// Article Start
server.post(base+"/articles", controllers.article.createArticle)
server.put(base+"/articles/:id", controllers.article.updateArticle)
server.del(base+"/articles/:id", controllers.article.deleteArticle)
server.get({path: base+"/articles/:id", version: "1.0.0"}, controllers.article.viewArticle)
server.get({path: base+"/articles/:lat/:lng/:radius", version: "1.0.0"}, controllers.article.fetchArticlesByRange)
server.get({path: base+"/articles/:page/:size", version: "1.0.0"}, controllers.article.fetchArticles)
//TODO find why the heck i was getting error when i named it articles/count
server.get({path: base+"/reports/count", version: "1.0.0"}, controllers.article.fetchArticlesCount)
server.get({path: base+"/reports/count/:key/:value", version: "1.0.0"}, controllers.article.fetchArticlesCountBy)
server.get({path: base+"/articlesby/:key/:value", version: "1.0.0"}, controllers.article.fetchArticlesBy)

// This is comment operations referenced in article
server.put("/articles/:id/comments", controllers.article.createArticleComment)
// Article End

// Comment Start
// You can also operate on commands in Comment resource. Some of the URI below, refers to above URIs for article
server.put(base+"/comments/:id", controllers.comment.updateComment)
server.del(base+"/comments/:id", controllers.comment.deleteComment)
server.get(base+"/comments/:id", controllers.comment.viewComment)
// Comment End

var port = process.env.PORT || 3000;
server.listen(port, function (err) {
    if (err)
        console.error(err)
    else
        console.log('App is ready at : ' + port)
})

if (process.env.environment == 'production')
    process.on('uncaughtException', function (err) {
        console.error(JSON.parse(JSON.stringify(err, ['stack', 'message', 'inner'], 2)))
    })