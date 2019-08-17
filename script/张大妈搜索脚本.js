// ==UserScript==
// @name         张大妈搜索过滤
// @namespace    http://tampermonkey.net/
// @version      0.1
// @description  对搜索进行过滤 删除无用数据
// @author       changdy
// @match        ://search.smzdm.com/*
// @grant        none
// ==/UserScript==

(function() {
  $("#J_back_top").click(_ => {
    let price = prompt("请输入值的比例", "10");
    if (price != null && price != "") {
      if (price === "0") {
        localStorage.removeItem("itemFilter");
        filterItem(0);
      } else {
        let tempItemp = {
          price: parseInt(price, 10),
          words: new URLSearchParams(window.location.search).get("s")
        };
        localStorage.setItem("itemFilter", JSON.stringify(tempItemp));
        filterItem(parseInt(price, 10));
      }
    }
  });

  initFilter();
  function initFilter() {
    let jsonString = localStorage.getItem("itemFilter");
    if (jsonString === null) {
      return;
    } else {
      let searchItem = JSON.parse(jsonString);
      let param = new URLSearchParams(window.location.search).get("s");
      if (searchItem.words === param) {
        filterItem(searchItem.price);
      } else {
        localStorage.removeItem("itemFilter");
      }
    }
  }

  function filterItem(price) {
    let itemsArr = $("#feed-main-list .feed-row-wide");
    let itemLength = itemsArr.length;
    for (let index = 0; index < itemLength; index++) {
      const priceValue = $(itemsArr[index])
        .find(".price-btn-up .unvoted-wrap span")
        .text();
      if (
        priceValue === undefined ||
        priceValue.includes("k") ||
        priceValue.includes("K") ||
        parseInt(priceValue, 10) >= price
      ) {
        $(itemsArr[index]).show();
      } else {
        $(itemsArr[index]).hide();
      }
    }
  }
})();
