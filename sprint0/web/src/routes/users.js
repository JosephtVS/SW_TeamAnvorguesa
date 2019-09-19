const express = require('express');
const router = express.Router();

router.get('/users/signin',(req,res)=>{
    res.render('users/signin');
});