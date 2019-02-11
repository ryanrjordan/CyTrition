<?php
$servername = "mysql.cs.iastate.edu";
$username = "dbu309yt8";
$password = "TarDx@ew";
$dbname = "db309yt8";
$conn = mysqli_connect($servername,$username,$password,$dbname);

if(!$conn) {
	die("Connection failed: " . mysqli_connect_error());
}

if(isset($_POST['login_id']) && isset($_POST['recipe_name']) && isset($_POST['ingredient_name'])) {
	$loginID = $_POST['login_id'];
    $recipeName = $_POST['recipe_name'];
	$ingredientName = $_POST['ingredient_name'];

	$query = "SELECT a.ingredient_id FROM Ingredient a WHERE a.ingredient_name = '$ingredientName';";
	$result = mysqli_query($conn, $query);
	if($result->num_rows > 0) {
		//WHERE NOT EXIST
		$first = "SELECT recipe_id FROM Recipe WHERE login_id = '$loginID' AND recipe_name = '$recipeName'";
		$second = "SELECT a.ingredient_id FROM Ingredient a WHERE a.ingredient_name = '$ingredientName'";
		$secondQuery = "INSERT INTO RecipeIngredient (recipe_id,ingredient_id) VALUES (($first), ($second));";
		if($conn->query($secondQuery)===TRUE) {
			echo "yes, ingredient name added to the database";
		}
		else {
			echo "failed1 to add ingredient";
		}
	}
	else {
		$firstQuery = "INSERT INTO Ingredient (ingredient_name) VALUES ('$ingredientName');";
		if($conn->query($firstQuery)===TRUE) {

			$first = "SELECT recipe_id FROM Recipe WHERE login_id = '$loginID' AND recipe_name = '$recipeName'";
			$second = "SELECT a.ingredient_id FROM Ingredient a WHERE a.ingredient_name = '$ingredientName'";
			$secondQuery = "INSERT INTO RecipeIngredient (recipe_id,ingredient_id) VALUES (($first), ($second));";
			if($conn->query($secondQuery)===TRUE) {
				echo "yes, ingredient name added to the database";
			}
			else {
				echo "failed2 to add ingredient";
			}
		}
		else {
			echo "failed3 to add new ingredient.";
		}
	}
}


$conn->close();
exit;
?>
