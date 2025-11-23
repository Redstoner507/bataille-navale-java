Yann STOLL-GEYER

Diego APARICIO

TP1

**Rapport des choix de conception**

Ce rapport présente les principaux choix de conception réalisés pour le modèle de notre bataille navale. L’objectif était de réaliser une bataille navale avec Java Swing en respectant l’architecture MVC.

**La Grille**

La classe Grid représente le plateau et encapsule les dimensions de la grille, les cellules, le placement d’objets, et les actions (tir, bombe, sonar).

**Les Cellules**

Les classes WaterCell et IslandCell, qui héritent de la classe abstraite Cell permettent d’avoir une double nature des cases (eau / île) et une future extension vers d’autres types de terrain sans toucher au reste du programme. Mais elles permettent surtout de faire en sorte de ne pas pouvoir placer de bateaux sur l’île ou d’armes dans l’eau. 

**Les objets plaçables**

Les objets pouvant être placés (bateaux, pièges, armes) ont beaucoup de logique en commun pour le placement donc ils héritent de la classe abstraite Placeable qui représente n’importe quel objet qui peut être placé dans des cellules de la grille. Cela permet d’unifier le placement sur la grille, de gérer orientation, taille, coordonnées, et de préparer naturellement l’arrivée d’autres objets (ex : mines, bonus…)

Chaque classe qui étend la classe Placeable précise son type de cellule autorisé, ce qui garantit des placements cohérents (exemple : les armes seulement sur l’île et les bateaux seulement sur l’eau).

**Les Bateaux**

Chaque bateau a exactement la même logique et a comme seules différences son type, sa taille et son nom. Nous avons donc choisi d’avoir une classe abstraite Ship, avec des sous-classes Carrier, Destroyer, etc., et un ShipFactory pour avoir un code lisible et mutualisé, un instanciateur propre qui limite les dépendances, et la possibilité d’ajouter facilement un nouveau type de bateau. Les bateaux gèrent leurs “points de vie” via _cellsRemaining, logique entièrement interne au modèle.

**Les Armes**

Les armes (classes Missile, Bomb et Sonar qui étendent la classe abstraite Weapon ) héritent de la classe Placeable). Chaque classe implémentant Weapon doit surcharger la méthode abstraite apply() pour définir l’action à effectuer quand cette arme est utilisée sur la grille. Ce choix permet une extensibilité totale et que chaque arme implémente sa logique indépendamment.

**Les Pièges**

De la même façon que pour les armes, les pièges (classes Bomb et Sonar qui étendent la classe abstraite Trap) héritent de Placeable et disposent en plus d’une méthode abstraite activate qui définit l’action à effectuer quand un joueur déclenche le piège. 

Nous avons un WeaponFactory et un TrapFactory pour instancier proprement les sous-classes en limitant les dépendances.

**Le Joueur et l’Ordinateur**

Nous avons créé une classe Player et utilisé le patron de conception Strategy (classes HumanStrategy, ComputerEasyStrategy, ComputerMediumStrategy, ComputerHardStrategy qui implémentent l’interface PlayerStrategy) pour pouvoir changer dynamiquement la logique de l’action réalisée à chaque tour. Au début du jeu on aurait deux instances de la classe Player, une avec la stratégie HumanStrategy (le joueur humain) et l’autre avec une stratégie ComputerEasy (ou ComputerMediumStrategy ou ComputerHardStrategy) que le joueur pourra choisir avant de lancer la partie. Le PlayerStrategyFactory permet une création standardisée.

**La Configuration de jeu**

La classe GameConfig permet de gérer et de stocker la configuration du jeu choisie par le joueur avant le début de la partie.
