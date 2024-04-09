import java.io.File

class Actions {
    var persons: ArrayList<Person> = ArrayList()
    var stringFromConsole: String = ""
    var start: Boolean = true
    fun enterInConsole(): String {
        stringFromConsole = readLine().toString()
        return stringFromConsole
    }
    fun readCommand() {
        print("Введите действие: 1. exit 2. help 3. add <Имя> phone <Номер телефона> 4. add <Имя> email <Адрес электронной почты> 5. show <Имя> 6. find <Номер телефона или Адрес электронной почты> 7. export <Путь к файлу>: ")
        val a: String = enterInConsole()
        val del = " "
        val command = a.split(del)

        if (command[0] == "exit") {
            val exit = Command.Exit()
            exit.isValid()
            newCommand(exit)
            start = false
        }
        if (command[0]  == "help") {
            val help = Command.Help()
            help.isValid()
            newCommand(help)
        }
        if (command[0] == "add" && command[2] == "phone") {
            val addPhone = Command.AddPhone(true)
            if (checkPhone()) {
                addPhone.validValue = true
                addPhone.isValid()
                newCommand(addPhone)
            }
            else {
                addPhone.validValue = false
                addPhone.isValid()
                println("Введен некорректный номер")
                help()
            }
        }
        if (command[0] == "add" && command[2] == "email") {
            val addEmail = Command.AddEmail(true)
            if (checkEmail()) {
                addEmail.validValue = true
                addEmail.isValid()
                newCommand(addEmail)
            }
            else {
                addEmail.validValue = false
                addEmail.isValid()
                println("Введена некорректная электронная почта")
                help()
            }
        }
        if (command[0]  == "show" && command.size> 1) {
            val show = Command.Show(true)
            newCommand(show)
        }
        if (command[0]  == "find" && command.size> 1) {
            val find = Command.Find(true)
            newCommand(find)
        }
        if (command[0]  == "export" && command.size> 1) {
            val export = Command.Export(true)
            newCommand(export)
        }

    }
    fun checkPhone(): Boolean {
        var a = stringFromConsole.split(" ")
        if(a[3].contains("+") && a[3].length == 12) {
            return true
        }
        else {
            return false
        }
    }
    fun checkEmail(): Boolean {
        var a = stringFromConsole.split(" ")
        if(a[3].contains("@") && a[3].contains(".")) {
            return true
        }
        else {
            return false
        }
    }

    fun help() {
        println("********************************************")
        println("Чтобы выйти, введите в консоль exit \n" +
                "Чтобы получить помощь, введите в консоль help \n" +
                "Чтобы добавить нового пользователя и его номер телефона, введите в консоль add <Имя> phone <Номер телефона> \n" +
                "Чтобы добавить нового пользователя и его электронную почту, введите в консоль add <Имя> email <Адрес электронной почты> \n" +
                "Чтобы посмотреть номера телефонов и электронную почту пользователя, введите show <Имя> \n" +
                "Чтобы найти пользователя по номеру телефона или электронной почте, введите find <Номер телефона или Адрес электронной почты>")
        println("********************************************")
    }
    fun exit() {
        println("Вы выбрали команду exit. До свидания!")

    }
    fun addPhone() {
        var a = stringFromConsole.split(" ")
        if (checkName(a[1], persons)) {
            for (person in persons) {
                if (person.name.contains(a[1])) {
                    person.phones.add(a[3])
                    println(person)
                }
            }
        }
        if (!checkName(a[1], persons)) {
            var phones: ArrayList<String?> = ArrayList()
            var emailes: ArrayList<String?> = ArrayList()
            phones.add(a[3])
            val person = Person(a[1], phones, emailes)
            persons.add(person)
            println(person)
        }
    }
    fun addEmail() {
        var a = stringFromConsole.split(" ")
        if (checkName(a[1], persons)) {
            for (person in persons) {
                if (person.name.contains(a[1])) {
                    person.emails.add(a[3])
                    println(person)
                }
            }
        }
        if (!checkName(a[1], persons)) {
            var phones: ArrayList<String?> = ArrayList()
            var emails: ArrayList<String?> = ArrayList()
            emails.add(a[3])
            val person = Person(a[1], phones, emails)
            persons.add(person)
            println(person)
        }
    }

    fun show() {
        var a = stringFromConsole.split(" ")
        if (checkName(a[1], persons)) {
            for (person in persons) {
                if (person.name.contains(a[1])) {
                    println(person)
                }
            }
        }
        if (!checkName(a[1], persons)) {
            println("Веденное имя отсутствует в справочнике")
        }

    }
    fun find() {
        var a = stringFromConsole.split(" ")
        if (persons.size >0) {
            for (person in persons) {
                if (person.phones.contains(a[1]) || person.emails.contains(a[1])) {
                    println(person.name)
                }
            }
        }
        if (persons.size ==0) {
            println("Справочник пуст")
        }
    }
    fun export() {
        try {
            var a = stringFromConsole.split(" ")
            writeToFile(a[1], writeToJson())
            println("Запись в файл прошла успешно")
        }
        catch (e: Exception) {
            println("Неверный путь к файлу")
        }
    }
    fun newCommand(command: Command) {
        when(command) {
            is Command.Exit -> exit()
            is Command.Help -> help()
            is Command.AddPhone -> addPhone()
            is Command.AddEmail -> addEmail()
            is Command.Show -> show()
            is Command.Find -> find()
            is Command.Export -> export()
        }
    }

    fun checkName(str: String, persons: ArrayList<Person>): Boolean {
        for (person in persons) {
            if (person.name.contains(str)) {
                return true
                break
            }
        }
        return false
    }
    /*
        Функция, преобразовывающая справочник в JSON-строку
     */
    fun writeToJson ():String {
        var jsonString: StringBuilder = StringBuilder()
        for (person in persons) {
            jsonString.append("{'name':'${person.name}','phone':'${person.phones.toString()}','emails':'${person.emails.toString()}'},")
        }
        jsonString.deleteCharAt(jsonString.length -1)
        return jsonString.toString()
    }
    /*
        Функция записи в файл
     */
    fun writeToFile(path: String, text: String) {
        File(path).writeText(text)
    }
}