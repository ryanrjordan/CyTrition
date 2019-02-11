<?php
$servername = "mysql.cs.iastate.edu";
$username = "dbu309yt8";
$password = "TarDx@ew";
$dbname = "db309yt8";
$conn = mysqli_connect($servername,$username,$password,$dbname);

if(!$conn) {
	die("Connection failed: " . mysqli_connect_error());
}

if(isset($_POST['report_id'])) {
	$report_id = $_POST['report_id'];
	
	$query = "DELETE FROM bug_reports WHERE report_id='$report_id';";
	if($conn->query($query)===TRUE) {
		echo "successful";
	} else {
		echo "Couldn't add to database";
	}
	
}

$conn->close();
exit;
?>
