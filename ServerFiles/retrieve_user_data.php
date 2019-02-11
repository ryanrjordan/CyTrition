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
		echo "password," . $row[2] . ",email," . $row[3] . ",name," . $row[4] . ",gender," 
		. $row[5] . ",height," . $row[6] . ",weight," . $row[7] . ",user_type," . $row[8] 
		. ",age," . $row[9] . ",";
	} else {
		echo "Can't find user data.";
	}
}


$conn->close();
exit;
?>