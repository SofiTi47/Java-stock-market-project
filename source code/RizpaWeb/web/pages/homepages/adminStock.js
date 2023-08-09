var refreshRate = 2000; //milli seconds
var refreshMessageRate = 15000;
var STOCK_INFO_URL = buildUrlWithContextPath("userStockTable");
var PREVIOUS_LIST_URL = buildUrlWithContextPath("transactionHistory");
var BUY_LIST_URL = buildUrlWithContextPath("buyList");
var SELL_LIST_URL = buildUrlWithContextPath("sellList");

$(function(){
    if (sessionStorage.getItem("username") == null){
        window.location.replace("../../index.html")
    }
    return false;
});

$(function () {
    var data = localStorage.getItem("symbol")
    console.log(data);
    if (data == null){
        console.log("no stock symbol")
        window.location.href = "../homepages/adminHomePage.html"
    }
    document.getElementById('symbolPlaceholder').innerHTML = data;
});

function refreshStocksInfo(stock) {
    $("#userStockTable tbody").empty();
    console.log(stock)
        var tr="<tr>";
        var td1="<td>"+stock.CompanyName+"</td>";
        var td2="<td>"+stock.stockSymbol+"</td>";
        var td3="<td>"+stock.stockRate+"</td>";
        var td4="<td>"+stock.accumTurnover+"</td>"
        $("#userStockTable").append(tr+td1+td2+td3+td4);
    }

    function ajaxStockInfo() {
        $.ajax({
            data: {stock: localStorage.getItem("symbol")},
            url: STOCK_INFO_URL,
            error: function() {
                //window.location.href = "../homepages/adminHomePage.html";
            },
            success: function(info) {
                refreshStocksInfo(info);
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
    function refreshBuyList(transactions) {

        $("#buyTable tbody").empty();

        for(var i =0; i< transactions.length; i++){
            console.log(transactions[i])
            var tr="<tr>";
            var td1="<td>"+transactions[i].timeStamp+"</td>"
            var td2= "<td>"+transactions[i].rate+"</td>"
            var td3 ="<td>"+transactions[i].stockAmount+"</td></tr>"
            $("#buyTable").append(tr+td1+td2+td3);
        }
    }
    function refreshSellList(transactions) {

        $("#sellTable tbody").empty();

        for(var i =0; i< transactions.length; i++){
            console.log(transactions[i])
            var tr="<tr>";
            var td1="<td>"+transactions[i].timeStamp+"</td>"
            var td2= "<td>"+transactions[i].rate+"</td>"
            var td3 ="<td>"+transactions[i].stockAmount+"</td></tr>"
            $("#sellTable").append(tr+td1+td2+td3);
        }
    }

    function ajaxPreviousList() {
        $.ajax({
            data: {stock: localStorage.getItem("symbol")},
            url: PREVIOUS_LIST_URL,
            error: function() {
                //window.location.href = "../homepages/adminHomePage.html";
            },
            success: function(transactions) {
                refreshPreviousList(transactions);
            }
        });
    }
    function ajaxBuyList() {
        $.ajax({
            data: {stock: localStorage.getItem("symbol")},
            url: BUY_LIST_URL,
            error: function() {
                //window.location.href = "../homepages/adminHomePage.html";
            },
            success: function(transactions) {
                refreshBuyList(transactions);
            }
        });
    }
    function ajaxSellList() {
        $.ajax({
            data: {stock: localStorage.getItem("symbol")},
            url: SELL_LIST_URL,
            error: function() {
                //window.location.href = "../homepages/adminHomePage.html";
            },
            success: function(transactions) {
                refreshSellList(transactions);
            }
        });
    }

    $(function() {
        setInterval(ajaxPreviousList,refreshRate);
        setInterval(ajaxSellList,refreshRate);
        setInterval(ajaxBuyList,refreshRate);
        setInterval(ajaxStockInfo,refreshRate);
        setInterval(clearMessage,refreshMessageRate);
    });




