package io.github.lucaargolo.seasons.mixed;

import io.github.lucaargolo.seasons.utils.SeasonalWeatherTable;
import net.minecraft.world.biome.Biome;

public interface BiomeMixed {

    Biome.Weather getOriginalWeather();

    void setOriginalWeather(Biome.Weather originalWeather);

    SeasonalWeatherTable getSeasonalWeatherTable();

    void setSeasonalWeatherTable(SeasonalWeatherTable seasonalWeatherTable);


}
