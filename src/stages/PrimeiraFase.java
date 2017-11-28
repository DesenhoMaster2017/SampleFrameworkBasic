package stages;

import scenes.GameScene;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import commands.Command;
import commands.CommandType;
import commands.MoveCommand;
import constants.WindowConstants;
import entity.Enemy;
import entity.GameEntity;
import entity.player.PlayerController;
import entity.pool.ObjectPool;
import game.World;
import game.evolver.GameEvent;
import game.evolver.GameEventCallback;
import util.RunEvent;


/**
 * 
 * @author Lucas Araujo, Lucas Pereira, Vinicius, Halê Valente, Miguel Nery
 *
 *	GameEntity: é a base para modelo de entidades do jogo, crie classes herdando
 *		seu comportamento e modifique, principalmente o método didContact, que 
 *		gerencia os eventos de colisão do jogo.
 *
 *  World: é aonde acontece a mágica, ele precisa ter seu update rodando sempre,
 *  	adicione entidades de forma simples e prática com o método add. Para criar novos
 *  	eventos use addEventAfterCurrentTime, ele chamará seu delegate (GameEventCallback) 
 *  	para cuidar dos eventos que forem disparados. Pode-se adicionar pools para gerenciar
 *  	a reciclagem de memória.
 *  	
 *  Enemy: são entidades baseadas a seguirem behavior, Behavior é um array de Commands que 
 *  	rodam funções internas específicas na entidade a quem behavior esta rodando.
 *  
 *  
 *  DoubleWingsFramework é uma framework de base para alguns dos básicos elementos de 
 *  desenvolvimento de jogos
 *  
 */
public class PrimeiraFase extends GameScene implements GameEventCallback{

	private World world;
	private PlayerController playerControl;

	
	@Override
	protected void initialSetup() {
		// TODO Auto-generated method stub
		world = new World();

		playerControl = new PlayerController();
		GameEntity player = new GameEntity("src/assets/img/player_lvl1.png");
		playerControl.entity = player;
		world.add(player);
		
		//Creating enemy pool
		this.setupEnemyRecycling();

		//Setup Input handle
		setupActions();
		
		//Setup event calls
		initEvents();
	}
	
	@Override
	protected void viewSetup() {
		
	}
	
	
	@Override
	public void update() {
		// TODO Auto-generated method stub
		world.update();
		playerControl.update();
	}
	
	
	
	
	
//MARK: Event evolver callback
	public void eventCallback(GameEvent event) {

  		switch (event.type){
  		case 1:
  			initEvents();
  			break;
  		case 2:
  			createEnemy1();
  			break;
  			
  		case 3:
  			Enemy e = this.behaviorEnemy();
  			world.add(e);
  			break;
  		default:
  			
  			break;
  		}
  	}
	
	public void initEvents(){
		this.world.addEventAfterCurrentTime(this, 700, 1, "Recursive script");
		this.world.addEventAfterCurrentTime(this, 200, 2, "Enemy1");
		this.world.addEventAfterCurrentTime(this, 500, 3, "Enemy1");
	}

	
	//Creating basic enemy without behavior
	public void createEnemy1(){
		
		//Creating enemy from pool
  		Enemy asteroid = (Enemy) world.createEntity(Enemy.class);
  		asteroid.setLife(10);
  		asteroid.x = WindowConstants.WIDTH/2 - asteroid.width/2;
  		asteroid.y = 0;
  		asteroid.vely = 2.0;

  		world.add(asteroid);
	}
	
	//Creating enemy with behavior 
	private Enemy behaviorEnemy() {
		
		// Behavior is a Set of commands
		ArrayList<Command> behavior = new ArrayList<Command>();
		behavior.add(new MoveCommand(CommandType.DOWN));
		behavior.add(new MoveCommand(CommandType.DOWN));
		behavior.add(new MoveCommand(CommandType.LEFT));
		behavior.add(new MoveCommand(CommandType.LEFT));
		behavior.add(new MoveCommand(CommandType.RIGHT));
		behavior.add(new MoveCommand(CommandType.RIGHT));
		behavior.add(new MoveCommand(CommandType.UP));
		
		
		//Creating enemy from pool
		Enemy enemy = (Enemy) world.createEntity(Enemy.class);
		enemy.x = Math.random() * (WindowConstants.WIDTH - enemy.width*2) + enemy.width;
  		enemy.y = 0;
  		enemy.vely = 1.0;
  		enemy.addBehavior(behavior);
  		enemy.startBehaving();
  		
  		return enemy;
	}
	
	
	// Setup player actions
	public void setupActions(){
		
		final int mv = 5;
		
		//Move up
		playerControl.addActionToKey(KeyEvent.VK_UP, 1, new RunEvent(){
			public void run(Object source){
				GameEntity p = (GameEntity) source;
				p.y -= mv;
			}
		});
		
		//Move down
		playerControl.addActionToKey(KeyEvent.VK_DOWN, 1, new RunEvent(){
			public void run(Object source){
				GameEntity p = (GameEntity) source;
				p.y += mv;
			}
		});
		
		//Move left
		playerControl.addActionToKey(KeyEvent.VK_LEFT, 1, new RunEvent(){
			public void run(Object source){
				GameEntity p = (GameEntity) source;
				p.x -= mv;
			}
		});
		
		//Move right
		playerControl.addActionToKey(KeyEvent.VK_RIGHT, 1, new RunEvent(){
			public void run(Object source){
				GameEntity p = (GameEntity) source;
				p.x += mv;
			}
		});
	}
	
	
	// Enemy pool
	public void setupEnemyRecycling(){
		
		//Percebe-se que depois de um tempo, nenhuma instância nova é criada
		//já se recicla antigas instâncias...
		this.world.addPool(new ObjectPool<Enemy>(){

			@Override
			protected Enemy create() {
				System.out.println("Enemy created...");
				return new Enemy("src/assets/img/asteroid.png");
			}

			@Override
			public Boolean shouldRecycle(Enemy obj) {
				
				if(obj.y > 500){
					System.out.println("Recycling enemy...");
					return true;
				}
				
				return false;
			}

			@Override
			public Class<Enemy> recyclingClass() {
				return Enemy.class;
			}
			
		});
		
		// O ideal é que deva ser criado uma classe a parte para
		// cada pool.
	}

}
