<?php 

	require './cron/Cron.php';

        $cron = new Cron();
        
        $cron->checkMessage();