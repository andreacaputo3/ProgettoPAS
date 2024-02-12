package it.unisalento.pas.smartcitywastemanagement.service;

import java.util.ArrayList;
import java.util.List;

import it.unisalento.pas.smartcitywastemanagement.domain.Bin;
import it.unisalento.pas.smartcitywastemanagement.domain.CleaningPath;
import it.unisalento.pas.smartcitywastemanagement.domain.Route;
import it.unisalento.pas.smartcitywastemanagement.repositories.CleaningPathRepository;
import it.unisalento.pas.smartcitywastemanagement.repositories.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CleaningPathService {

    @Autowired
    private RouteRepository routeRepository;

    public List<CleaningPath> generateCleaningPaths(List<Bin> bins) {
        List<CleaningPath> cleaningPaths = generateSimpleCleaningPaths(bins);

        return cleaningPaths;
    }

    public List<Route> generateRoutes(List<Bin> bins) {
        List<Route> routes = new ArrayList<>();
        int position = 0;
        for (Bin bin : bins) {
            Route route = new Route();
            route.setBinId(bin.getId());
            route.setPosition(position);
            routes.add(route);
            position++;
        }
        return routes;
    }

    public List<Route> saveRoutes(List<Route> routes) {
        return routeRepository.saveAll(routes);
    }

    public List<Route> getRoutesByBinId(String binId) {
        return routeRepository.findByBinId(binId);
    }

    private List<CleaningPath> generateSimpleCleaningPaths(List<Bin> bins) {
        // Collega i cassonetti consecutivamente per formare i percorsi

        List<CleaningPath> cleaningPaths = new ArrayList<>();

        for (int i = 0; i < bins.size() - 1; i++) {
            Bin startBin = bins.get(i);
            Bin endBin = bins.get(i + 1);

            // Crea un nuovo percorso di pulizia che collega startBin a endBin
            CleaningPath cleaningPath = new CleaningPath();
            cleaningPath.setStartBin(startBin);
            cleaningPath.setEndBin(endBin);
            cleaningPath.setDistance(calculateDistance(startBin, endBin));

            cleaningPaths.add(cleaningPath);
        }

        return cleaningPaths;
    }

    private double calculateDistance(Bin startBin, Bin endBin) {
        final int R = 6371; // Raggio della Terra in chilometri

        double lat1 = Math.toRadians(startBin.getLatitude());
        double lon1 = Math.toRadians(startBin.getLongitude());
        double lat2 = Math.toRadians(endBin.getLatitude());
        double lon2 = Math.toRadians(endBin.getLongitude());

        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distance = R * c; // Distanza in chilometri

        return distance;
    }

    public List<Route> getAllRoutes() {
        return routeRepository.findAll();
    }

}
