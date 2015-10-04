//TODO why did this not work for imports so simple thing find out youve workedo n js so much assho
var utils = {
convertDates : function(data){
    var dateFormat = "dd.mm.yyyy";

    data.dateOfIncident = stringToDate(this.dateOfIncident,dateFormat,".");
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

            return formatedDate;
}

