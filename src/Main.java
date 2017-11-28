
import game.Game;
import scenes.menu.MenuScene;
import scenes.stages.stage1.StageTest;
import stages.PrimeiraFase;

public class Main {

	public static void main(String[] args) {
		
		System.out.println("Initializing...");
		
		//Creating first scene as menu
		MenuScene menu = new MenuScene();
		menu.firstLevel = new PrimeiraFase();
		
		Game game = new Game();
		game.setFirstScene( menu );
		
		game.start();
	}

}
