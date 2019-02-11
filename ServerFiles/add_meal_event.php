<?php
$servername = "mysql.cs.iastate.edu";
$username = "dbu309yt8";
$password = "TarDx@ew";
$dbname = "db309yt8";
$conn = mysqli_connect($servername,$username,$password,$dbname);

if(!$conn) {
	die("Connection failed: " . mysqli_connect_error());
}

if(isset($_POST['login_id']) && isset($_POST['date']) && isset($_POST['meal_type'])) {
	$login_id = $_POST['login_id'];
	$date = $_POST['date'];
	$meal_type = $_POST['meal_type'];
	
	$query = "INSERT INTO calendar_meal_events (login_id,date_logged,meal_type) VALUES ('$login_id', '$date','$meal_type');";
	if($conn->query($query)===TRUE) {
		$get = "SELECT * FROM calendar_meal_events WHERE BINARY login_id='$login_id' AND date_logged='$date' AND meal_type='$meal_type';";
		$result = mysqli_query($conn,$get);
		$row = mysqli_fetch_row($result);
		echo $row[3];
	} else {
		echo "Couldn't add to database";
	}
	
}

$conn->close();
exit;
?>
