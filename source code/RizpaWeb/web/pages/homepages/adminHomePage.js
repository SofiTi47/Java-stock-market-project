var refreshRate = 2000; //milli seconds
var USER_LIST_URL = buildUrlWithContextPath("userslist");
var STOCK_TABLE_URL = buildUrlWithContextPath("stocktable");


$(function(){
    if (sessionStorage.getItem("username") == null){
        window.location.replace("../../index.html")
    }
    return false;
});
//users = a list of usernames, essentially an array of javascript strings:
// ["moshe","nachum","nachche"...]
function refreshUsersList(users) {
    //clear all current users
    $("#userslist tbody").empty();

    // rebuild the list of users: scan all users and add them to the list of users
    $.each(users || [], function(index, user) {
        console.log("Adding user #" + index + ": " + user);
        const myArr = user.split(" ");
        var tr ="<tr>"
        var td1 = "<td>"+myArr[0]+"</td>";
        var td2 = "<td>"+myArr[1]+"</td></tr>";
        //create a new <li> tag with a value in it and append it to the #userslist (div with id=userslist) element
        $("#userslist").append(tr+td1+td2);
    });
}
function refreshTableView(stocks){
    $("#stocktable tbody").empty();
    $.each(stocks || [], function(index,stock) {
        var tr="<tr>";
        var td1="<td>"+stock.CompanyName+"</td>";
        var td2="<td>"+'<div class="sym" onclick="stockUrl(this.innerText)">' +stock.stockSymbol
            +"</div></td>";
        var td3="<td>"+stock.stockRate+"</td>";
        var td4="<td>"+stock.accumTurnover+"</td></tr>"
        $("#stocktable").append(tr+td1+td2+td3+td4);
    })
}
function stockUrl(symbol) {
    localStorage.setItem("symbol",symbol);
    window.location.href = "../homepages/adminStock.html";
    return true
}
function ajaxTableView(){
    $.ajax({
        url:STOCK_TABLE_URL,
        success:function (stocks){
            refreshTableView(stocks);
        }
    });
}
function ajaxUsersList() {
    $.ajax({
        url: USER_LIST_URL,
        success: function(users) {
            refreshUsersList(users);
        }
    });
}
//activate the timer calls after the page is loaded
$(function() {
    //The users list is refreshed automatically every second
    setInterval(ajaxUsersList,refreshRate);
    setInterval(ajaxTableView,refreshRate);
});



