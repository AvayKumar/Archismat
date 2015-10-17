<?php
/**
 * Description of index
 * 
 * @author avay
 */

session_start();

    $css_links = '<link href="./css/bootstrap.min.css" rel="stylesheet">'
                . '<link href="./css/login.css" rel="stylesheet">';
    $js_links = '';
    
    require './views/components/header.php';
    
    require './views/login.php';
    
    require './views/components/footer.php';