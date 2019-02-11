<?php
$servername = "mysql.cs.iastate.edu";
$username = "dbu309yt8";
$password = "TarDx@ew";
$dbname = "db309yt8";
$conn = mysqli_connect($servername,$username,$password,$dbname);
if(!$conn) {
	die("Connection failed: " . mysqli_connect_error());
}

if(isset($_POST['login_id']) && isset($_POST['date'])) {
	$loginid = $_POST['login_id'];
	$date = $_POST['date'];
	$query = "SELECT * FROM calendar_meal_events WHERE BINARY login_id = '$loginid' AND BINARY date_logged = '$date'";
	$result = mysqli_query($conn,$query);
	
	$row_cnt = mysqli_num_rows($result);
	echo $row_cnt;
}

$conn->close();
exit;
?>