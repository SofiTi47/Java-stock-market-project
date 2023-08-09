var refreshRate = 2000; //milli seconds
var refreshMessageRate = 15000;
var STOCK_INFO_URL = buildUrlWithContextPath("userStockTable");
var PREVIOUS_LIST_URL = buildUrlWithContextPath("transactionHistory");
var UPDATE_LIST_URL = buildUrlWithContextPath("updateList");

$(function(){
    if (sessionStorage.getItem("username") == null){
        window.location.replace("../../index.html")
    }
    return false;
});

$(function () {
    var data = localStorage.getItem("symbol")
    if (data == null){
        console.log("no stock symbol")
        window.location.href = "../homepages/brokerHomePage.html"
    }
    document.getElementById('symbolPlaceholder').innerHTML = data;
    document.getElementById("symbolHolder").value = data;
});

function refreshStockInfo(stock) {
    $("#userStockTable tbody").empty();
    console.log(stock);
        var tr="<tr>";
        var td1="<td>"+stock.CompanyName+"</td>";
        var td2="<td>"+stock.stockSymbol+"</td>";
        var td3="<td>"+stock.stockRate+"</td>";
        var td4="<td>"+stock.accumTurnover+"</td>"
        var td5="<td>"+stock.userHolding+"</td></tr>"
        $("#userStockTable").append(tr+td1+td2+td3+td4+td5);
}

function ajaxStockInfo() {
    $.ajax({
        data: {stock: localStorage.getItem("symbol")},
        url: STOCK_INFO_URL,
        timeout: 2000,
        error: function() {
            window.location.href = "../homepages/brokerHomePage.html";
        },
        success: function(info) {
            refreshStockInfo(info);
        }
    });

}

function refreshPreviousList(transactions) {

    $("#transactionHistory tbody").empty();

    for(var i =0; i< transactions.length; i++){
        console.log(transactions[i])
        var tr="<tr>";
        var td1="<td>"+transactions[i].timeStamp+"</td>"
        var td2= "<td>"+transactions[i].rate+"</td>"
        var td3 ="<td>"+transactions[i].stockAmount+"</td></tr>"
        $("#transactionHistory").append(tr+td1+td2+td3);
    }
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

function ajaxPreviousList() {
    $.ajax({
        data: {stock: localStorage.getItem("symbol")},
        url: PREVIOUS_LIST_URL,
        timeout: 2000,
        error: function() {
             window.location.href = "../homepages/brokerHomePage.html";
        },
        success: function(info) {
            refreshPreviousList(info);
        }
    });
}

function ajaxUpdateList() {
    $.ajax({
        url: UPDATE_LIST_URL,
        success: function(updates) {
            refreshUpdatesList(updates);
        }
    });
}

$(function() {
    setInterval(ajaxPreviousList,refreshRate);
    setInterval(ajaxStockInfo,refreshRate);
    setInterval(clearMessage,refreshMessageRate);
    setInterval(ajaxUpdateList,refreshRate);
});

function priceRequired(){
    if (document.getElementById("mkt").checked){
        document.getElementById("rate").required = false;
        return;
    }
    document.getElementById("price").required = true;
}

function clearMessage(){
    document.getElementById("Error-Placeholder").innerText = "";
}


$(function() {
    $('#transactionForm').ajaxForm({
        success: function() {
            document.getElementById("Error-Placeholder").innerText = "Transaction added. " +
                "Check notification for more info.";
            document.getElementById("transactionForm").reset();
        },
        error: function() {
            document.getElementById("Error-Placeholder").innerText = "Error."
        }
    });
    return false;
});



