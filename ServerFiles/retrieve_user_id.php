<?php
$servername = "mysql.cs.iastate.edu";
$username = "dbu309yt8";
$password = "TarDx@ew";
$dbname = "db309yt8";
$conn = mysqli_connect($servername,$username,$password,$dbname);
if(!$conn) {
	die("Connection failed: " . mysqli_connect_error());
}


if(isset($_POST['login_id'])) {
	$loginid = $_POST['login_id'];
	$query = "SELECT * FROM user_profile_table WHERE BINARY login_id = '$loginid';";
	$result = mysqli_query($conn,$query);
	if($result->num_rows > 0) {
		$row = mysqli_fetch_row($result);
		echo $row[0];
	} else {
		echo "Can't find user data.";
	}
}

$conn->close();
exit;
?>