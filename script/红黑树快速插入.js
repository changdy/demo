// ==UserScript==
// @name         红黑树快速点击
// @namespace    https://github.com/changdy
// @version      0.1
// @description  主要是为了方便一次性插入元素
// @author       changdy
// @match        https://www.cs.usfca.edu/~galles/visualization/RedBlack.html
// @grant        none
// ==/UserScript==

(function () {
    // 想达到当RedBlack 实例调用 enableUI方法的时候 执行另外一段代码
    let functionf = RedBlack.prototype.enableUI;
    let arr, index, stopFlag = true;
    RedBlack.prototype.enableUI = function () {
        functionf.call(this);
        setTimeout(() => {
            appenChild();
        }, 200);
    };
    $("#header").click(x => {
        arr = prompt("请输入数组", "").replace(/ /g, '').split(",");
        stopFlag = false;
        index = 0;
        appenChild();
    });

    function appenChild() {
        if (stopFlag === false) {
            $("#AlgorithmSpecificControls > td:nth-child(1) > input[type=Text]")[0].value = arr[index++];
            $('#AlgorithmSpecificControls > td:nth-child(2) > input[type=Button]').click();
            if (index === arr.length) {
                stopFlag = true;
            }
        }
    }
})();