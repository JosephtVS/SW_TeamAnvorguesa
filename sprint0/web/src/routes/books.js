const express = require('express');
const router = express.Router();

//const Book = require('../models/Book');

router.get('/books/add',(req, res)=>{
    res.render('books/new-book');
});

module.exports = router;