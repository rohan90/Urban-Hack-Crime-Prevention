var mongoose = require("mongoose");
var Schema   = mongoose.Schema;
var Comment = require("./Comment");

var ArticleSchema = new Schema({
    id:String,
    title: String,
    content: String,
    tags: [String],
    category: String,
    role:String,
    
    author: String,
    authorImgUrl: String,
    comments: [Comment.schema],

    imgUrls:[String],
    videoUrl:String,
    
    addressOfIncident:String,
    descriptionOfIncident:String,
    typeOfPlace:String,
    
    lattitude:Number,
    longitude:Number,
    loc:[Number,Number],
    
    cctvFootageAvailable:String,
    
    reportId:String,
    divison: String,
    firStatus:String,
    policeStation:String,
    gender: String,
    victimsAge:{type:Number,min:0,max:110},
    
    assailantVehicleType:String,
    assailantVehicleModel:String,
    assailatVehicleColor:String,
    assailantVehicleNumber:String,
    assailantWearingHelmet:String,

    dateOfIncident:Number,
    timeOfOccurencce:String,
    timeBetween:String,
    dateOfReport:Number,

    month:String,
    day:String,

    createdOn    : { type: Date },
  	updatedOn    : { type: Date }

});

ArticleSchema.pre('save', function(next){

  now = new Date();
  this.updatedOn = now;
  if ( !this.createdOn ) {
    this.createdOn = now;
  }
  this.loc=[this.lattitude,this.longitude];
  this.id = this._id;
  next();
});

mongoose.model('Article', ArticleSchema);

