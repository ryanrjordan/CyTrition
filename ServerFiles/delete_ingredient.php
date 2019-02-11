<?php
$servername = "mysql.cs.iastate.edu";
$username = "dbu309yt8";
$password = "TarDx@ew";
$dbname = "db309yt8";
$conn = mysqli_connect($servername,$username,$password,$dbname);

if(!$conn) {
	die("Connection failed: " . mysqli_connect_error());
}

if(isset($_POST['login_id']) && isset($_POST['ingredient_name']) && isset($_POST['recipe_name'])) {
	$login_id = $_POST['login_id'];
	$ingredient_name = $_POST['ingredient_name'];
	$recipe_name = $_POST['recipe_name'];

	//Get ingredient_id
    $query = "SELECT ingredient_id FROM Ingredient WHERE ingredient_name = '$ingredient_name';";
    $result = mysqli_query($conn,$query);
	if ($result->num_rows > 0) {
        $row =  mysqli_fetch_row($result);
        $ingredient_id = $row[0];

		//Get recipe_id
		$query2 = "SELECT recipe_id FROM Recipe WHERE login_id = '$login_id' AND recipe_name = '$recipe_name';";
		$result2 = mysqli_query($conn,$query2);
		if ($result2->num_rows > 0) {
	        $row2 =  mysqli_fetch_row($result2);
	        $recipe_id = $row2[0];

			$query3 = "DELETE FROM RecipeIngredient WHERE recipe_id='$recipe_id' AND ingredient_id='$ingredient_id';";
			if($conn->query($query3)===TRUE) {
				echo "yes";
			} else {
				echo "no";
			}

		}

	}

}


$conn->close();
exit;
?>
