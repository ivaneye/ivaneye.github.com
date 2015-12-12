/**
 * Created by ivan on 15-6-19.
 */

$(function () {
    //构建左目录树
    var _toc1 = $("<ul class='sectlevel1'></ul>");//一级目录ul
    var _innerToc1;  //二级目录对应的li
    var _toc2 = $("<ul class='sectlevel2'></ul>");//二级目录ul
    var innerT2;  //二级目录对应的li
    var _t1;            //一级目录对应的index
    var _t2;            //二级目录对应的index
    var name; //构建的id
    $("#content h1, h2").each(function (i) {
        var _this = $(this);
        _this.attr("id", "title" + i);
        var _cont = _this.html();
        var _tag = _this.prop("tagName");
        if (_tag == "H1") {
            if (i == 0) {
                _t1 = i + 1;
            } else {
                _t1 += 1;
            }
            _t2 = 0;
            name = _t1 + ". " + _cont;
            if(innerT2 != null){
                _innerToc1.append(_toc2);
                _toc2 = $("<ul class='sectlevel2'></ul>");
                innerT2 = null;
            }
            _innerToc1 = $("<li><a href='#title" + i + "'>" + name + "</a></li>");
            _toc1.append(_innerToc1);
        } else {
            _t2 += 1;
            name = _t1 + "." + _t2 + ". " + _cont;
            innerT2 = $("<li><a href='#title" + i + "'>" + name + "</a></li>");
            _toc2.append(innerT2);
        }
        _this.html(name);
    });
    if(innerT2 != null){
        _innerToc1.append(_toc2);
        innerT2 = null;
    }

    $("#toc").append(_toc1);
})