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
        <li @click="`location.href=/book?path=${Base64.encode(chapter.path)}`">{{chapter.title}}</li>
    </ul>
</div>
<script>
    var bookPath = getQueryVariable('path');
    var vm = new Vue({
        el: '#app',
        data: {
            chapters: [],
            queryIndex: 0,
            noMore: false
        },
        methods: {
            gotoChapter(book) {
                location.href = `/chapter?path=${Base64.encode(book.path)}`
            },
            appendChapters() {
                var that = this;
                $.get(`/mds/${bookPath}?offset=${queryIndex}`, function (data) {
                    if (data && data.length > 0) {
                        that.chapters = that.chapters.concat(data);
                        that.queryIndex = that.queryIndex + data.length;
                        setTimeout(function () {
                            that.appendBooks()
                        }, 200)
                    } else {
                        that.noMore = true
                    }
                })
            }
        },
        mounted() {
            this.appendChapters();
        }
    })
</script>
</body>
</html>