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
    <title>books</title>
</head>
<body>
<div id="app">
    <ul class="books" v-for="(book, idx) in books">
        <li @click="gotoBook(book)">{{book.title}}</li>
    </ul>
</div>
<script>
    var vm = new Vue({
        el: '#app',
        data: {
            books: [],
            queryIndex: 1,
            noMore: false
        },
        methods: {
            gotoBook(book) {
                location.href = `/book?path=${Base64.encode(book.path)}`
            },
            appendBooks() {
                var that = this;
                $.get('/mds/' + Base64.encode(this.queryIndex), function (data) {
                    if (data && data.length > 0) {
                        that.books = that.books.concat(data);
                        that.queryIndex = that.queryIndex + 1;
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
            this.appendBooks();
        }
    })
</script>
</body>
</html>