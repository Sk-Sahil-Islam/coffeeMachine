enum class Beverage(val visibleName: String, val water:Int, val milk:Int, val beans: Int, val price: Int) {
    ESPRESSO("espresso", 250, 0 , 16, 4),
    LATTE("latte", 350, 75, 20, 7),
    CAPPUCCINO("cappuccino", 200, 100, 12, 6),
}
class CoffeeMachine(var water: Int, var milk: Int, var beans: Int, var cups: Int, var money: Int){
    fun getStatus(): String {
        return "The coffee machine has:\n" +
                "$water of water\n" +
                "$milk of milk\n" +
                "$beans of coffee beans\n" +
                "$cups of disposable cups\n" +
                "$money of money"
    }
    fun buy(beverage: Beverage): String{
        return when {
            water < beverage.water -> "Sorry, not enough water!"
            milk < beverage.milk -> "Sorry, not enough milk!"
            beans < beverage.beans -> "Sorry, not enough coffee beans!"
            cups < 1 -> "Sorry, not enough disposable cups!"
            else -> {
                water -= beverage.water
                milk -= beverage.milk
                beans -= beverage.beans
                cups--
                money += beverage.price
                "I have enough resources, making you a coffee!"
            }
        }
    }
}
class CoffeeMachineInterface{
    enum class State { MAIN,BUY, FILL_WATER, FILL_MILK, FILL_BEANS, FILL_CUPS, END }
    private val beverageString = Beverage.values().joinToString { "${it.ordinal + 1} - ${it.visibleName}" }
    private var state = State.MAIN
    private val coffeeMachine = CoffeeMachine(400, 540, 120, 9, 550)
    fun getPrompt(): String {
        return when(state) {
            State.MAIN -> "Write action (buy, fill, take, remaining, exit): "
            State.BUY -> "What do you want to buy? $beverageString, back - to main menu: "
            State.FILL_WATER -> "Write how many ml of water do you want to add: "
            State.FILL_MILK -> "Write how many ml of milk do you want to add: "
            State.FILL_BEANS -> "Write how many grams of coffee beans do you want to add: "
            State.FILL_CUPS -> "Write how many disposable cups of coffee do you want to add: "
            State.END -> ""
        }
    }
    fun process(input: String): String {
        when (state) {
            State.MAIN -> when (input) {
                "buy" -> {
                    state = State.BUY
                }
                "fill" -> {
                    state = State.FILL_WATER
                }
                "take" -> {
                    val output = "I gave you $${coffeeMachine.money}"
                    coffeeMachine.money = 0
                    return output
                }
                "remaining" -> {
                    return coffeeMachine.getStatus()
                }
                "exit" -> {
                    state = State.END
                }
                else -> return "Invalid Input"
            }
            State.BUY -> {
                if (input != "back") coffeeMachine.buy(Beverage.values()[input.toInt() - 1])
                state = State.MAIN
            }
            State.FILL_WATER -> {
                coffeeMachine.water += input.toInt()
                state = State.FILL_MILK
            }
            State.FILL_MILK -> {
                coffeeMachine.milk += input.toInt()
                state = State.FILL_BEANS
            }
            State.FILL_BEANS -> {
                coffeeMachine.beans += input.toInt()
                state = State.FILL_CUPS
            }
            State.FILL_CUPS -> {
                coffeeMachine.cups += input.toInt()
                state = State.MAIN
            }
            State.END -> check(false)
        }
        return ""
    }

    fun isRunning() = state != State.END
}
fun main(){
    val coffeeMachineInterface = CoffeeMachineInterface()
    while(coffeeMachineInterface.isRunning()){
        println(coffeeMachineInterface.getPrompt())
        val input = readln()
        println(coffeeMachineInterface.process(input))
    }
}