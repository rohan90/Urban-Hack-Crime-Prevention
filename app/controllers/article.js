var mongoose = require('mongoose'),
    Article = mongoose.model("Article"),
    Comment = mongoose.model("Comment"),
    ObjectId = mongoose.Types.ObjectId;

var utils = {
convertDates : function(data){
    try{
    var dateFormat = "dd.mm.yyyy";
    var timeFormat = "HH:mm:ss"
    if(typeof data.dateOfIncident === "string"){
        console.log("date sent is of string type so ..")
    data.dateOfIncident = stringToDate(data.dateOfIncident,dateFormat,".");
    data.dateOfReport = stringToDate(data.dateOfReport,dateFormat,".");
    }else{
        console.log("date sent is not string type so ..safe T_T")
    }
    //data.timeOfOccurencce = timeToDate(data.timeOfOccurencce,timeFormat,":");
    //data.timeBetween = timeToDate(data.timeBetween,timeFormat,":");
    }catch(err){
        console.log("date parsing is risky!!")
    }
    return data;
}
}

function stringToDate(_date,_format,_delimiter)
{
    console.log("converting date: "+_date +"\n")
            var formatLowerCase=_format.toLowerCase();
            var formatItems=formatLowerCase.split(_delimiter);
            var dateItems=_date.split(_delimiter);
            var monthIndex=formatItems.indexOf("mm");
            var dayIndex=formatItems.indexOf("dd");
            var yearIndex=formatItems.indexOf("yyyy");
            var month=parseInt(dateItems[monthIndex]);
            month-=1;
            var formatedDate = new Date(dateItems[yearIndex],month,dateItems[dayIndex]);
    console.log("converted date: "+formatedDate +"\n")

            return formatedDate.getTime();
}

function timeToDate(_date,_format,_delimiter)
{
    console.log("converting time: "+_date +"\n")
            var formatLowerCase=_format.toLowerCase();
            var formatItems=formatLowerCase.split(_delimiter);
            var hourIndex=formatItems.indexOf("HH");
            var minuteIndex=formatItems.indexOf("mm");
            var secondIndex=formatItems.indexOf("ss");
            
            var formatedDate = new Date();
            formatedDate.setHours(hourIndex);
            formatedDate.setMinutes(minutesIndex);
            formatedDate.setSeconds(secondIndex);
    console.log("converted time: "+formatedDate +"\n")

            return formatedDate;
}


exports.createArticle = function(req, res, next) {
    var data = utils.convertDates(req.body);
    var articleModel = new Article(data);
    articleModel.save(function(err, article) {
        if (err) {
            res.status(500);
            res.json({
                type: false,
                data: "Error occured: " + err
            })
        } else {
            res.json({
                type: true,
                data: article
            })
        }
    })
}

exports.fetchArticles = function(req, res, next) {
    Article.find(function(err, article) {
        if (err) {
            res.status(500);
            res.json({
                type: false,
                data: "Error occured: " + err
            })
        } else {
            if (article) {
                res.json({
                    type: true,
                    size: article.length,
                    data: article
                })
            } else {
                res.json({
                    type: false,
                    data: "Article: " + req.params.id + " not found"
                })
            }
        }
    })
}

exports.fetchArticlesByRange = function(req, res, next) {
    var lat = req.params.lat;
    var lng = req.params.lng;
    var radius = req.params.radius;

    Article.find({
  loc: { $geoWithin: { $centerSphere: [ [ lat, lng ], radius/3963.2 ] } }
},function(err, article) {
        if (err) {
            res.status(500);
            res.json({
                type: false,
                data: "Error occured: " + err
            })
        } else {
            if (article) {
                res.json({
                    type: true,
                    size: article.length,
                    data: article
                })
            } else {
                res.json({
                    type: false,
                    data: "Article: " + req.params.id + " not found"
                })
            }
        }
    })
}

exports.viewArticle = function(req, res, next) {
    Article.findById(new ObjectId(req.params.id), function(err, article) {
        if (err) {
            res.status(500);
            res.json({
                type: false,
                data: "Error occured: " + err
            })
        } else {
            if (article) {
                res.json({
                    type: true,
                    data: article
                })
            } else {
                res.json({
                    type: false,
                    data: "Article: " + req.params.id + " not found"
                })
            }
        }
    })
}

exports.viewArticle_v2 = function(req, res, next) {
    Article.findById(new ObjectId(req.params.id), function(err, article) {
        if (err) {
            res.status(500);
            res.json({
                type: false,
                data: "Error occured: " + err
            })
        } else {
            if (article) {
                article.title = article.title + " v2"
                res.json({
                    type: true,
                    data: article
                })
            } else {
                res.json({
                    type: false,
                    data: "Article: " + req.params.id + " not found"
                })
            }
        }
    })
}

exports.updateArticle = function(req, res, next) {
    var updatedArticleModel = new Article(req.body);
    var articleWithRemovedId = updatedArticleModel.toObject();
    delete articleWithRemovedId["_id"];

    Article.findByIdAndUpdate(new ObjectId(req.params.id), articleWithRemovedId, function(err, article) {
        if (err) {
            res.status(500);
            res.json({
                type: false,
                data: "Error occured: " + err
            })
        } else {
            if (article) {
                res.json({
                    type: true,
                    data: article
                })
            } else {
                res.json({
                    type: false,
                    data: "Article: " + req.params.id + " not found"
                })
            }
        }
    })
}

exports.deleteArticle = function(req, res, next) {
    Article.findByIdAndRemove(new Object(req.params.id), function(err, article) {
        if (err) {
            res.status(500);
            res.json({
                type: false,
                data: "Error occured: " + err
            })
        } else {
            res.json({
                type: true,
                data: "Article: " + req.params.id + " deleted successfully"
            })
        }
    })
}

exports.createArticleComment = function(req, res, next) {
    Article.findOne({_id: new ObjectId(req.params.id)}, function(err, article) {
        if (err) {
            res.json({
               type: false,
                data: "Error occured: " + err
            });
        } else {
            if (article) {
                var commentModel = new Comment(req.body);
                article.comments.push(commentModel);
                article.save(function(err, result) {
                    res.json({
                       type: true,
                        data: result
                    });
                });
            } else {
                res.json({
                    type: false,
                    data: "Article: " + req.params.id + " not found"
                });
            }
        }


    })

}
