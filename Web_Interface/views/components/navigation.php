    
    <?php
        
        if( isset( $_POST['loggout'] ) ) {
            
            session_unset();
            session_destroy();
            $_SESSION['admin'] = false;
            header('Location: index.php');
            
        }
    
    ?>
    
    <nav class="navbar navbar-default navbar-inverse navbar-fixed-top">
      <div class="container">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="javascript:void(0);">Archismat</a>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
          <ul class="nav navbar-nav">
            <li><a href="<?php echo BASE_URL.'add-event.php' ?>">Add Event</a></li>
            <li><a href="<?php echo BASE_URL.'add-picture.php' ?>">Add Picture</a></li>
            <li><a href="<?php echo BASE_URL.'add-alert.php' ?>">Add Alert</a></li>
            <li class="dropdown">
              <a href="javascript:void(0);" class="dropdown-toggle" data-toggle="dropdown" role="button">Browse <span class="caret"></span></a>
              <ul class="dropdown-menu" role="menu">
                <li><a href="<?php echo BASE_URL.'browse-event.php' ?>">Event</a></li>
                <li><a href="<?php echo BASE_URL.'browse-pick.php' ?>">Picture</a></li>
                <li><a href="<?php echo BASE_URL.'browse-alert.php' ?>">Alert</a></li>
                <li><a href="<?php echo BASE_URL.'browse-devices.php' ?>">Browse registered devices</a></li>
              </ul>
            </li>
          </ul>
          <ul class="nav navbar-nav navbar-right">
              <li><a href="javascript:void(0);" onclick="document.getElementById('logg-out').click()">Log Out</a></li>
          </ul>
        </div>
      </div>
    </nav>
    <form  method="post">
        <input name="loggout" type="hidden">
        <input id="logg-out" style="display: none;" type="submit">
    </form>