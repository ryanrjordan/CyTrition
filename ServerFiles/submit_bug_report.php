<?php
$servername = "mysql.cs.iastate.edu";
$username = "dbu309yt8";
$password = "TarDx@ew";
$dbname = "db309yt8";
$conn = mysqli_connect($servername,$username,$password,$dbname);

if(!$conn) {
	die("Connection failed: " . mysqli_connect_error());
}

if(isset($_POST['poster_id']) && isset($_POST['report_text'])) {
	$poster_id = $_POST['poster_id'];
	$report_text = $_POST['report_text'];
	
	$query = "INSERT INTO bug_reports (poster_id,report_text) VALUES ('$poster_id', '$report_text');";
	if($conn->query($query)===TRUE) {
		echo "successful";
	} else {
		echo "Couldn't add to database";
	}
	
}

$conn->close();
exit;
?>
