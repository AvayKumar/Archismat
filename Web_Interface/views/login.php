<div class="container">

    <form class="form-signin" method="post">
    <h2 class="form-signin-heading">Please sign in</h2>
    <?php 

    if( isset( $_POST['submit'] ) ) {

        if( isset( $_POST['user-name'] ) && isset( $_POST['password'] ) ) {
            
            if( $_POST['user-name'] == 'admin' && $_POST['password'] == 'archismat' ) {
                session_start();
                $_SESSION['admin'] = true;
                header('Location: add-event.php');
            } else {
                $_SESSION['admin'] = false;
                echo '<div class="alert alert-danger" role="alert">Invalid user name or Password</div>';
            }
        }

    }

    ?>
    <label for="inputEmail" class="sr-only">Email address</label>
    <input type="text" id="inputEmail" class="form-control" name="user-name" placeholder="User Name" required autofocus>
    <label for="inputPassword" class="sr-only">Password</label>
    <input type="password" id="inputPassword" class="form-control" name="password" placeholder="Password" required>
    <button class="btn btn-lg btn-primary btn-block" name="submit" type="submit">Sign in</button>
  </form>

</div>