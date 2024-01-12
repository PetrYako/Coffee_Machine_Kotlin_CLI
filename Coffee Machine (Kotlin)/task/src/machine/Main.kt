package machine

enum class States {
    WAITING_FOR_ACTION,
    CHOOSE_ACTION,
    CHOOSE_TYPE_OF_COFFEE,
    ADD_WATER,
    ADD_MILK,
    ADD_BEANS,
    ADD_CUPS,
    OFF
}

class Coffee(val name: String, val water: Int, val milk: Int = 0, val beans: Int, val cost: Int)

class CoffeeMachine(var water: Int, var milk: Int, var beans: Int, var cups: Int, var money: Int) {
    val availableCoffee = listOf(
        Coffee(name = "Espresso", water = 250, beans = 16, cost = 4),
        Coffee(name = "Latte", water = 350, milk = 75, beans = 20, cost = 7),
        Coffee(name = "Cappuccino", water = 200, milk = 100, beans = 12, cost = 6)
    )
    var state = States.CHOOSE_ACTION

    fun handle(input: String = "") {
        if (input == "exit") {
            state = States.OFF
            return
        }

        if (input == "back") {
            state = States.WAITING_FOR_ACTION
            handle()
        }

        when (state) {
            States.WAITING_FOR_ACTION -> {
                println()
                println("Write action (buy, fill, take, remaining, exit):")
                state = States.CHOOSE_ACTION
            }

            States.CHOOSE_ACTION -> {
                when (input) {
                    "buy" -> {
                        println()
                        println("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino:")
                        state = States.CHOOSE_TYPE_OF_COFFEE
                    }

                    "fill" -> {
                        println()
                        println("Write how many ml of water you want to add:")
                        state = States.ADD_WATER
                    }

                    "take" -> {
                        println()
                        println("I gave you $$money")
                        money = 0
                        state = States.WAITING_FOR_ACTION
                        handle()
                    }

                    "remaining" -> {
                        println()
                        println("The coffee machine has:")
                        println("$water ml of water")
                        println("$milk ml of milk")
                        println("$beans g of coffee beans")
                        println("$cups disposable cups")
                        println("$$money of money")
                        state = States.WAITING_FOR_ACTION
                        handle()
                    }
                }
            }

            States.CHOOSE_TYPE_OF_COFFEE -> {
                val coffee = availableCoffee[input.toInt() - 1]
                when {
                    water < coffee.water -> println("Sorry, not enough water!")
                    milk < coffee.milk -> println("Sorry, not enough milk!")
                    beans < coffee.beans -> println("Sorry, not enough beans!")
                    cups <= 0 -> println("Sorry, not enough cups!")
                    else -> {
                        println("I have enough resources, making you a coffee!")
                        water -= coffee.water
                        milk -= coffee.milk
                        beans -= coffee.beans
                        cups--
                        money += coffee.cost
                    }
                }
                state = States.WAITING_FOR_ACTION
                handle()
            }

            States.ADD_WATER -> {
                water += input.toInt()
                println("Write how many ml of milk you want to add:")
                state = States.ADD_MILK
            }

            States.ADD_MILK -> {
                milk += input.toInt()
                println("Write how many grams of coffee beans you want to add:")
                state = States.ADD_BEANS
            }

            States.ADD_BEANS -> {
                beans += input.toInt()
                println("Write how many disposable cups you want to add:")
                state = States.ADD_CUPS
            }

            States.ADD_CUPS -> {
                cups += input.toInt()
                state = States.WAITING_FOR_ACTION
                handle()
            }

            States.OFF -> return
        }
    }
}

fun main() {
    val machine = CoffeeMachine(400, 540, 120, 9, 550)
    println("Write action (buy, fill, take, remaining, exit):")
    while (machine.state != States.OFF) {
        val input = readln()
        machine.handle(input)
    }
}
