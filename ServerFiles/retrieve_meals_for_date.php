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
	if($result->num_rows > 0) {
		while($row =  mysqli_fetch_row($result)) {
	        echo $row[2] . "-" . $row[3] . ",";
		}
	} else {
		echo "Can't find any valid meal data.";
	}
}

$conn->close();
exit;
?>