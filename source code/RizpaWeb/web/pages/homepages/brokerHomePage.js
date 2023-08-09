var refreshRate = 2000; //milli seconds
var USER_LIST_URL = buildUrlWithContextPath("userslist");
var STOCK_TABLE_URL = buildUrlWithContextPath("stocktable");
var USER_FUNDS_URL = buildUrlWithContextPath("totalFunds");
var USER_HISTORY = buildUrlWithContextPath("historytable");
var UPDATE_LIST_URL = buildUrlWithContextPath("updateList");


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
    window.location.href = "../homepages/tradePage.html";
    return true
}
function refreshHistoryView(transactions){
    $("#historytable tbody").empty();
    for(var i =0; i< transactions.length; i++){
        var tr="<tr>";
        var td1="<td>"+transactions[i].transactionType+"</td>"
        var td2="<td>"+transactions[i].timeStamp+"</td>"
        var td3= "<td>"+transactions[i].transactionTotal+"</td>"
        var td4 ="<td>"+transactions[i].balanceBefore+"</td>"
        var td5 ="<td>"+transactions[i].balanceAfter+"</td></tr>"
        $("#historytable").append(tr+td1+td2+td3+td4+td5);
    }
}
function refreshFunds(funds){
    document.getElementById("totalFunds").innerText = "Funds: " + funds;
}
function refreshUpdatesList(updates) {
    //clear all current users
    $("#updateList tbody").empty();
    console.log(updates)
    for(var i =0; i< updates.length; i++){
        console.log(updates[i])
        var tr="<tr>";
        var td1="<td>"+updates[i]+"</td></tr>"
        $("#updateList").append(tr+td1);
    }
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
function ajaxUserFunds(){
    $.ajax({
        url: USER_FUNDS_URL,
        success :function(funds){
            refreshFunds(funds);
        }
    })
}
function ajaxHistoryTable(){
    $.ajax({
        url:USER_HISTORY,
        success:function(transactions){
            refreshHistoryView(transactions);
        }
    })
}
function ajaxUpdateList() {
    $.ajax({
        url: UPDATE_LIST_URL,
        success: function(updates) {
            refreshUpdatesList(updates);
        }
    });
}
//activate the timer calls after the page is loaded
$(function() {
    //The users list is refreshed automatically every second
    setInterval(ajaxUsersList,refreshRate);
    setInterval(ajaxTableView,refreshRate);
    setInterval(ajaxUserFunds,refreshRate);
    setInterval(ajaxHistoryTable,refreshRate);
    setInterval(ajaxUpdateList,refreshRate);
});
$(function() {
    $('#ReadXMLFile').ajaxForm({
        success: function() {
            document.getElementById("XML-Error-Placeholder").innerText = "File loaded successfully.";
            document.getElementById("ReadXMLFile").reset();
        },
        error: function(msg) {
            document.getElementById("XML-Error-Placeholder").innerText = msg;
        }
    });
    return false;
});
$(function(){
    $('#addStock').ajaxForm({
        success: function(){
            document.getElementById("Stock-Error-Placeholder").innerText = "Stock added successfully.";
            document.getElementById("addStock").reset();
        },
        error: function(){
            document.getElementById("Stock-Error-Placeholder").innerText = "Stock already exists."
        }
    });
    return false;
})
$(function(){
    $('#addFunds').ajaxForm({
        success: function(funds){
            document.getElementById("totalFunds").innerText = "Funds: " + funds;
            document.getElementById("addFunds").reset();
            document.getElementById("Funds-Error-Placeholder").innerText = "";
        },
        error: function(){
            document.getElementById("Funds-Error-Placeholder").innerText = "Error, Please try again."
        }
    });
    return false;
})
//localStorage.setItem(key,data);  move data between pages, session map
//localStorage.getItem(key);



