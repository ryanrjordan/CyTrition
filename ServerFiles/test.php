<?php
$servername = "mysql.cs.iastate.edu";
$username = "dbu309yt8";
$password = "TarDx@ew";
$dbname = "db309yt8";
$conn = mysqli_connect($servername,$username,$password,$dbname);

if(!$conn) {
	die("Connection failed: " . mysqli_connect_error());
}


	$login_id = 'ryan';
	$date = '3/26/2018';
	$meal_type = '3';
	
	$get = "SELECT * FROM calendar_meal_events WHERE login_id='ryan' AND date_logged='3/26/2018' AND meal_type='3'";
	$result = mysqli_query($conn,$get);
	
	if($result->num_rows > 0) {
		$row = mysqli_fetch_row($result);
		echo $row[3];
	} else {
		echo "Couldn't add to database";
	}

$conn->close();
exit;
?>
