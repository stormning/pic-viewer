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
</div>
<style type="text/css">
    .pics {
        margin: 0 auto;
        max-width: 800px;
        border-left: 1px solid #999;
        border-right: 1px solid #999;
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
            noMore: false
        },
        methods: {
            gotoBook(book) {
                location.href = `/chapter?path=${Base64.encode(book.path)}`
            },
            appendPics() {
                var that = this;
                $.get(`/files/${chapterPath}?offset=${that.offset}`, function (data) {
                    if (data && data.length > 0) {
                        that.pics = that.pics.concat(data);
                        that.offset = that.offset + 1;
                        setTimeout(function () {
                            that.appendPics()
                        }, 200)
                    } else {
                        that.noMore = true
                    }
                })
            }
        },
        mounted() {
            this.appendPics();
        }
    })
</script>
</body>
</html>