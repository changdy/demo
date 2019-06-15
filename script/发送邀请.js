// ==UserScript==
// @name             cmct邮箱批量发送
// @version          0.1
// @description      如标题,批量发送功能
// @author           changdy
// @license          GPL
// @date             2018-06-03
// @match            *://hdcmct.org/invite.php*
// ==/UserScript==

(function() {
    'use strict';



const sendUrl = "https://hdcmct.org/takeinvite.php?id=" + getUrlParam("id");
let emailList = [];
$('#outer').append(`
<div id="batch-mail">
    <textarea id="mail-list" rows="8" cols="80" placeholder="输入邮箱地址,换行符分割" style="margin-top:20px"></textarea>
    <br/>
    <button class="btn">发送邀请</button>
    <table style="margin-top:20px"><tbody id="send-info"></tbody></table>
</div>
`)

$("#outer button").click(() => {
    emailList = $("#mail-list").attr("disabled", true).val().split('\n');
    $("#send-info").empty();
    sendInvition(0);
})

function sendInvition(index) {
    if (index < emailList.length) {
        let tempMail = emailList[index].trim();
        if (tempMail === "") {
            sendInvition(++index);
        } else {
            $.ajax({
                url: sendUrl,
                data: {
                    email: tempMail,
                    body: `来自熊熊的邀请`,
                    probation: "yes"
                },
                dataType: "html",
                type: 'POST',
            }).then(x => {
                let info;
                if (x.trim() == "") {
                    info="发送成功";
                } else {
                    info = $($.parseHTML(x)).find('#outer .text').text();
                }
                $('#send-info').append(`<tr><td>${tempMail}</td><td>${info}</td></tr>`)
            }).fail(() =>
                $('#send-info').append(`<tr><td>${tempMail}</td><td>发送失败</td></tr>`)
            ).always(() => {
                sendInvition(++index);
            });
        }
    } else {
        $("#mail-list").attr("disabled", false);
    }
}

function getUrlParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]);
    return null;
}
})();