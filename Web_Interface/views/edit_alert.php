<div class="container">
    <?php echo $server_response; ?>
    <form method="post" role="form">
                    <div class="row">
                        <div class="col-sm-6">
                            <div class="form-group">
                                <div class="input-group">
                                    <input type="date" class="form-control" name="alert-date" value="<?php echo $alert_date; ?>" required>
                                    <div class="input-group-addon">Publish Date</div>
                                </div>
                            </div>
                        </div>
                        <div class="col-sm-6">
                            <div class="form-group">
                                <div class="input-group">
                                    <input type="time" class="form-control" name="alert-time" value="<?php echo $alert_time; ?>" required>
                                    <div class="input-group-addon">Publish Time</div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                      <textarea class="form-control" name="alert-description" placeholder="Alert body" rows="5" required><?php echo $alert_description; ?></textarea>
                    </div>
                    <input type="submit" name="submit" class="btn btn-primary" value="Submit">
                </form>
</div>