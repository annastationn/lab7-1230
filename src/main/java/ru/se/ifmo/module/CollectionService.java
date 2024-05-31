package ru.se.ifmo.module;


import ru.se.ifmo.exception.EmptyFieldException;
import ru.se.ifmo.exception.NegativeFieldException;
import ru.se.ifmo.model.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;

import static ru.se.ifmo.model.OrganizationType.*;


public class CollectionService {
    private final Date initializationDate;
    private final User user;
    protected LinkedHashMap<Long, Organization> collection;

    protected Scanner inputScanner;

    public CollectionService(User user) {
        collection = new LinkedHashMap<>();
        this.initializationDate = new Date();
        this.user = user;
    }

    public void init(Collection<Organization> organizations) {
        Organization[] organizationsArray = organizations.toArray(new Organization[]{});
        for (int i = 0; i < organizations.size(); i++) {
            collection.put((long) i+1, organizationsArray[i]);
        }
    }

    public void addElement(Long key) {
        Organization organization = createElement();
        organization.setUser(user);
        Long maxId = 0L;
        for (Map.Entry<Long, Organization> organizationEntry : collection.entrySet()) {
            if (organizationEntry.getValue().getId() > maxId) {
                maxId = organizationEntry.getValue().getId();
            }
        }
        organization.setId(++maxId);
        if (collection.containsKey(key)) {
            LinkedHashMap<Long, Organization> tempCollection = new LinkedHashMap<>();
            boolean inserted = false;

            for (Map.Entry<Long, Organization> entry : collection.entrySet()) {
                if (!inserted && entry.getKey() >= key) {
                    tempCollection.put(key, organization);
                    inserted = true;
                }
                tempCollection.put(entry.getKey() + (inserted ? 1 : 0), entry.getValue());
            }

            collection.clear();
            collection.putAll(tempCollection);
        } else if (!key.equals(collection.size() + 1L) && !collection.containsKey(key - 1)) {
            System.out.println("Невозможно вставить ключ " + key + ". Ключ должен быть следующим по номеру или вставлен между существующими ключами.");
        } else {
            // Если ключ не существует и он валидный, просто добавляем новый элемент
            collection.put(key, organization);
        }
    }

    public void info() {
        System.out.println("Тип коллекции: " + collection.getClass().getName());
        System.out.println("Дата создания: " + initializationDate);
        System.out.println("Количество элементов: " + collection.size());
    }

    public void show() {
        if (collection.isEmpty()){
            System.out.println("There is no elements in collection yet");
        } else{
            for (Map.Entry<Long,Organization> set : collection.entrySet()){
                System.out.println(set.getKey() + " - " + set.getValue().toString() + "\n");
            }
        }
    }

    public void update(long current_id) {
        if (!collection.containsValue(getElementById(collection, current_id))) {
            System.out.println("Элемент с таким id не существует");
            return;
        }
        if (!getElementById(collection, current_id).getUser().equals(user)) {
            System.out.println("Вы не можете обновить этот элемент так как он принадлежит другому пользователю");
            return;
        }
        for (Map.Entry<Long, Organization> o : collection.entrySet()) {
            if (current_id == o.getValue().getId()) {
                // Создаем новый объект с обновленными данными, но с тем же id
                Organization newElement = createElement();
                newElement.setId(current_id);
                // Заменяем старый объект новым, сохраняя исходный ключ
                collection.put(o.getKey(), newElement);
                System.out.println("Элемент с id " + current_id + " обновлен успешно");
                break;
            }
        }
    }

    public void removeKey(long key) {
        if (!collection.containsKey(key)) {
            System.out.println("Элемента с таким ключом не существует");
            return;
        }
        if (!collection.get(key).getUser().equals(user)) {
            System.out.println("Вы не можете удалить элемент который вам не принадлежит");
            return;
        }
        collection.remove(key);
        List<Map.Entry<Long, Organization>> entries = new ArrayList<>(collection.entrySet());
        for (Map.Entry<Long, Organization> entry : entries) {
            if (entry.getKey() > key) {
                Organization org = collection.remove(entry.getKey());
                collection.put(entry.getKey() - 1, org);
            }
        }
        System.out.println("Элемент с key " + key + " успешно удалён");
    }


    private void resolveKeys() {
        List<Map.Entry<Long, Organization>> entries = new ArrayList<>(collection.entrySet());
        collection.clear();
        long newKey = 1;
        for (Map.Entry<Long, Organization> entry : entries) {
            collection.put(newKey++, entry.getValue());
        }
    }

    public void clear(){
        collection.values().removeIf(organization -> organization.getUser().equals(user));
        resolveKeys();
        System.out.println("Все элементы принадлежащие вам успешно удалены");
    }

    public void removeLower() {
        Organization ref = createElement();
        collection.values().removeIf(organization -> organization.getUser().equals(user) && organization.compareTo(ref) < 0);
        resolveKeys();
        System.out.println("Все элеменеты меньше заданного удалены");
    }

    public void removeGreaterKey(long key) {
        collection.entrySet().removeIf(longOrganizationEntry -> longOrganizationEntry.getValue().getUser().equals(user) && longOrganizationEntry.getKey() > key);
        resolveKeys();
        System.out.printf("Все элеменеты с ключом больше чем %d удалены%n", key);
    }

    public void minByName() {
        if (!collection.isEmpty()) {
            Organization minEntry = null;
            for (Map.Entry<Long, Organization> set : collection.entrySet()) {
                if (minEntry == null || set.getValue().getName().length() < minEntry.getName().length()) {
                    minEntry = set.getValue();
                }
            }
            System.out.println("Минимальный элемент коллекции по имени: " + minEntry.toString());
        }
        else{
            System.out.println("Коллекция пуста!");
        }
    }

    public void filterGreaterThanType(OrganizationType standard){
        boolean hasItem = false;
        for (Map.Entry<Long,Organization> el : collection.entrySet()) {
            if (el.getValue().getType().equals(standard)) {
                hasItem = true;
                break;
            }
        }
        if (hasItem){
            for (Map.Entry<Long,Organization> o : collection.entrySet()) {
                if (String.valueOf(standard).length() < String.valueOf(o.getValue().getType()).length()){
                    System.out.println(o.getValue().toString() + "\n");
                }
            }
        } else {
            System.out.println("Organizations with such standard of living don't exist");
        }
    }

    public void printUniqueAnnualTurnover() {
        if (!collection.isEmpty()) {
            TreeSet<Float> set = new TreeSet<>();
            for (Map.Entry<Long, Organization> o : collection.entrySet()) {
                set.add(o.getValue().getAnnualTurnover());
            }
            for (Float num : set) {
                System.out.println("Unique turnover: " + num);
            }
        }
        else{
            System.out.println("Коллекция пуста!");
        }

    }
    private String askString (Scanner InputScanner) {
        while(true) {
            try {
                String name = InputScanner.nextLine().trim();
                if (name.isBlank()) {
                    throw new EmptyFieldException("Поле не может быть пустым. Введите его ещё раз: ");
                }
                return name;
            } catch (EmptyFieldException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    private float askX(Scanner InputScanner) {
        while(true) {
            try {
                return Float.parseFloat(InputScanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Неверный формат числа. Введите его повторно: ");
            }
        }
    }
    private double askY(Scanner InputScanner) {
        while(true) {
            try {
                return Double.parseDouble(InputScanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Неверный формат числа. Введите его повторно: ");
            }
        }
    }
    private float askFloat (Scanner InputScanner) {
        while (true) {
            try {
                float num = Float.parseFloat(InputScanner.nextLine());
                if (num > 0) {
                    return num;
                } else {
                    throw new NegativeFieldException("Число не может быть отрицательным. Введите его ещё раз: ");
                }}
            catch(NumberFormatException e){
                System.out.println("Неверный формат числа. Введите его повторно: ");
            } catch(NegativeFieldException e){
                System.out.println(e.getMessage());
            }
        }
    }
    private OrganizationType askOrganizationType(Scanner InputScanner) {
        while (true) {
            try{
                String type = InputScanner.nextLine().toUpperCase();
                return switch (type) {
                    case "COMMERCIAL" -> COMMERCIAL;
                    case "GOVERNMENT" -> GOVERNMENT;
                    case "TRUST" -> TRUST;
                    case "PRIVATE_LIMITED_COMPANY" -> PRIVATE_LIMITED_COMPANY;
                    case "OPEN_JOINT_STOCK_COMPANY" -> OPEN_JOINT_STOCK_COMPANY;
                    default ->
                            throw new EmptyFieldException("Такого типа организации не существует. " + "Заполните тип корректно: ");
                };
            } catch (EmptyFieldException e){
                System.out.println(e.getMessage());
            }
        }
    }

    public Organization createElement(){
        inputScanner = PromptScanner.getUserScanner();

        System.out.println("Введите имя");
        String name = askString(inputScanner);

        System.out.println("Введите координату x:");
        float x = askX(inputScanner);

        System.out.println("Введите координату y:");
        double y = askY(inputScanner);

        Coordinates coordinates = new Coordinates((long) x, (int) y);

        ZonedDateTime creationDate = ZonedDateTime.of(LocalDateTime.now(), ZoneOffset.UTC);

        System.out.println("Введите годовой оборот");
        float annualTurnover = askFloat(inputScanner);

        System.out.println("Введите адрес организации");
        Address officialAddress = new Address(askString(inputScanner));

        System.out.print("""
                Введите один из доступных типов организации:
                 COMMERCIAL,
                 GOVERNMENT,
                 TRUST,
                 PRIVATE_LIMITED_COMPANY,
                 OPEN_JOINT_STOCK_COMPANY
                """);
        OrganizationType organizationType = askOrganizationType(inputScanner);
        return new Organization(0L, name, coordinates, creationDate, annualTurnover, organizationType, officialAddress, user);
    }

    public Organization getElementById(LinkedHashMap<Long,Organization> collection, Long id) {
        for (Map.Entry<Long,Organization> o : collection.entrySet()) {
            if (Objects.equals(o.getValue().getId(), id)) {
                return o.getValue();
            }
        }
        return null;
    }

    public LinkedHashMap<Long, Organization> getCollection() {
        return collection;
    }
}