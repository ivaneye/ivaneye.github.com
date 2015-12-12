/**
 * Created by ivan on 15-6-19.
 */

$(function () {

    $("div.highlight pre").attr("class","pygments highlight");
    $("div.highlight").each(function(){
        var _this = $(this);
        var inner = _this.html();
        _this.empty();
        var _t = $("<div class='content'></div>");
        _t.append(inner);
        _this.append(_t);

        var lang = _this.find("code").attr("data-lang");
    });
    $("div.highlight").attr("class","listingblock");
})