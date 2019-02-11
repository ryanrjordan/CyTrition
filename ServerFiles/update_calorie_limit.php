<?php
$servername = "mysql.cs.iastate.edu";
$username = "dbu309yt8";
$password = "TarDx@ew";
$dbname = "db309yt8";
$conn = mysqli_connect($servername,$username,$password,$dbname);

if(!$conn) {
	die("Connection failed: " . mysqli_connect_error());
}

if(isset($_POST['user_id']) && isset($_POST['cal_amount'])) {
	$user_id = $_POST['user_id'];
	$cal_amount = $_POST['cal_amount'];
	
	$check = "SELECT * FROM user_calorie_limit WHERE BINARY user_id = '$user_id';";
	$result = mysqli_query($conn,$check);
	$query = "INSERT INTO user_calorie_limit (user_id,daily_calorie_amount) VALUES ('$user_id', '$cal_amount');";
	if($result->num_rows > 0) {
		$query = "UPDATE user_calorie_limit SET daily_calorie_amount='$cal_amount' WHERE user_id='$user_id';";
	}
	if($conn->query($query)===TRUE) {
		echo "successful";
	} else {
		echo "Couldn't update database";
	}
}

$conn->close();
exit;
?>
