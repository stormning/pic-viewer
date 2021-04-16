<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <script src="https://cdn.jsdelivr.net/npm/vue@2.6.12"></script>
    <script src="/js/jquery-3.6.0.min.js"></script>
    <script src="/js/base64.js"></script>
    <script src="/js/core.js"></script>
    <title>books</title>
</head>
<body>
<div id="app">
    <div class="pics">
        <img :src="`data:image/png;base64,${pic}`" v-for="(pic, idx) in pics"/>
    </div>
    <div class="no-more" v-if="noMore">
        <div class="op" @click="goto(prev)" v-if="prev">上一章</div>
        <div class="op" @click="goto(next)" v-if="next">下一章</div>
    </div>
</div>
<style type="text/css">
    body {
        padding: 0;
        margin: 0;
    }

    .pics, .no-more {
        margin: 0 auto;
        max-width: 800px;
        border-left: 1px solid #999;
        border-right: 1px solid #999;
    }

    .no-more{
        display: flex;
    }

    .no-more .op{
        flex: 1 1 auto;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 18px;
        font-weight: 400;
        height: 60px;
        border: 1px solid #999;
    }

    .pics img {
        display: block;
        width: 100%;
    }
</style>
<script>
    var chapterPath = getQueryVariable('path');
    var vm = new Vue({
        el: '#app',
        data: {
            pics: [],
            offset: 0,
            prev: null,
            next: null,
            noMore: false
        },
        methods: {
            goto(path) {
                location.href = `/chapter?path=${Base64.encode(path)}`
            },
            appendPics() {
                var that = this;
                $.get(`/files/${chapterPath}?offset=${that.offset}&limit=10`, function (data) {
                    if (data && data.length > 0) {
                        that.pics = that.pics.concat(data);
                        that.offset = that.offset + 10;
                    } else {
                        that.noMore = true
                    }
                })
            },
            initPrevNext(){
                var that = this;
                $.get(`/md-prev-next/${chapterPath}`, function (data) {
                    if (data[0]){
                        that.prev = data[0].path;
                    }
                    if (data[1]){
                        that.next = data[1].path;
                    }
                })
            },
            autoScroll() {
                var that = this;
                $(window).scroll(function () {
                    var scrollTop = $(this).scrollTop();
                    var scrollHeight = $(document).height();
                    var windowHeight = $(this).height();
                    if (scrollTop + windowHeight + 15 >= scrollHeight) {
                        that.appendPics();
                    }
                });
            }
        },
        mounted() {
            this.appendPics();
            this.initPrevNext();
            this.autoScroll();
        }
    })
</script>
</body>
</html>