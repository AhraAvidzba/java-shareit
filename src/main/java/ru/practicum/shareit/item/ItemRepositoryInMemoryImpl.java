//package ru.practicum.shareit.item;
//
//import org.springframework.stereotype.Repository;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Objects;
//import java.util.stream.Collectors;
//
//@Repository
//public class ItemRepositoryInMemoryImpl implements ItemRepository {
//    private final Map<Long, Item> items = new HashMap<>();
//    private Long globalId = 1L;
//
//    @Override
//    public Item saveItem(Item item) {
//        item.setId(generateId());
//        items.put(item.getId(), item);
//        return item;
//    }
//
//    @Override
//    public Item patchItem(Item item) {
//        items.put(item.getId(), item);
//        return items.get(item.getId());
//    }
//
//    @Override
//    public Item getItemById(Long itemId) {
//        return items.get(itemId);
//    }
//
//    @Override
//    public List<Item> getItemsOfUser(Long userId) {
//        return items.values().stream()
//                .filter(x -> Objects.equals(x.getOwner().getId(), userId))
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public List<Item> searchItems(String text) {
//        String lowText = text.toLowerCase();
//        return items.values().stream()
//                .filter(x -> x.getName().toLowerCase().contains(lowText)
//                        || x.getDescription().toLowerCase().contains(lowText))
//                .filter(Item::getAvailable)
//                .collect(Collectors.toList());
//    }
//
//    private Long generateId() {
//        return globalId++;
//    }
//}
