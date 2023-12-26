package com.geektrust.backend.repositories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.geektrust.backend.entities.MetroCard;

public class MetroCardRepository implements IMetroCardRepository {

    private Map<String, MetroCard> metroCardMap;
    private Integer autoIncrement = 0;

    public MetroCardRepository() {
        this.metroCardMap = new HashMap<>();
    }

    @Override
    public MetroCard save(MetroCard entity) {
        if (entity.getId() == null) {
            autoIncrement++;
            MetroCard metroCard = new MetroCard(String.valueOf(autoIncrement), entity.getBalance(),
                    entity.getTotalJourney());

            metroCardMap.put(metroCard.getId(), metroCard);
            return metroCard;
        }
        metroCardMap.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public List<MetroCard> findAll() {
        return (List<MetroCard>) metroCardMap.values();
    }

    @Override
    public Optional<MetroCard> findById(String id) {
        return metroCardMap.values().stream().filter(m -> m.getId().equals(id)).findFirst();
    }

    @Override
    public boolean existsById(String id) {
        if (metroCardMap.values().stream().filter(m -> m.getId().equals(id)).findFirst()
                .isPresent()) {
            return true;
        }
        return false;
    }

    @Override
    public void delete(MetroCard entity) {
        if (entity.getId() != null) {
            metroCardMap.remove(entity.getId());
        }

    }

    @Override
    public void deleteById(String metroCardId) {

        metroCardMap.remove(metroCardId);

    }

    @Override
    public long count() {
        return metroCardMap.size();
    }


}
