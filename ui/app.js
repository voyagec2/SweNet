// web.js
// Express is our web server that can handle request
var express = require('express');
var app = express();
var port = process.env.PORT || 8080;
var isDev = !('PORT' in process.env);
var webdir = isDev? 'web': 'dist';

var responseAppPage = function (req, res) {
    res.sendFile([__dirname, webdir, 'index.html'].join('/'));
};

app.get('/', responseAppPage);
app.get('/ui/*', responseAppPage);
app.get('*', express.static([__dirname, webdir].join('/')));

app.listen(port);
