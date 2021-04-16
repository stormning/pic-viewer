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
    <ul class="books" v-for="(chapter, idx) in chapters">
        <li @click="gotoChapter(chapter)">{{chapter.title}}</li>
    </ul>
</div>
<script>
    var bookPath = getQueryVariable('path');
    var vm = new Vue({
        el: '#app',
        data: {
            chapters: [],
            offset: 0,
            noMore: false
        },
        methods: {
            appendChapters() {
                var that = this;
                $.get(`/mds/${bookPath}?offset=${that.offset}`, function (data) {
                    if (data && data.length > 0) {
                        that.chapters = that.chapters.concat(data);
                        that.offset = that.offset + data.length;
                        setTimeout(function () {
                            that.appendChapters()
                        }, 200)
                    } else {
                        that.noMore = true
                    }
                })
            },
            gotoChapter(chapter) {
                location.href = `/chapter?path=${Base64.encode(chapter.path)}`
            }
        },
        mounted() {
            this.appendChapters();
        }
    })
</script>
</body>
</html>