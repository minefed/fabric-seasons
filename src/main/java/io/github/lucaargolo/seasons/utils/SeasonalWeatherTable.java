package io.github.lucaargolo.seasons.utils;

import io.github.lucaargolo.seasons.FabricSeasons;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.world.biome.Biome;

/**
 * Results of {@link FabricSeasons#getSeasonWeather(ModConfig, Season, Identifier, Boolean, float)} for every
 * season, for one config instance, biome id and original weather. getSeasonWeather only reads its arguments
 * and the config, so the table is valid for as long as {@link #matches} holds. Immutable (final fields filled
 * in the constructor), so it can be shared between server, render and chunk builder threads.
 */
public final class SeasonalWeatherTable {

    private final ModConfig config;
    private final Identifier biomeId;
    private final Biome.Weather originalWeather;
    private final boolean originalPrecipitation;
    private final int originalTemperatureBits;
    private final boolean[] precipitation;
    private final float[] temperature;

    public SeasonalWeatherTable(ModConfig config, Identifier biomeId, Biome.Weather originalWeather) {
        this.config = config;
        this.biomeId = biomeId;
        this.originalWeather = originalWeather;
        this.originalPrecipitation = originalWeather.hasPrecipitation;
        float originalTemperature = originalWeather.temperature;
        this.originalTemperatureBits = Float.floatToRawIntBits(originalTemperature);
        Season[] seasons = Season.values();
        this.precipitation = new boolean[seasons.length];
        this.temperature = new float[seasons.length];
        for (Season season : seasons) {
            Pair<Boolean, Float> weather = FabricSeasons.getSeasonWeather(config, season, biomeId, originalPrecipitation, originalTemperature);
            this.precipitation[season.ordinal()] = weather.getLeft();
            this.temperature[season.ordinal()] = weather.getRight();
        }
    }

    public boolean matches(ModConfig config, Identifier biomeId, Biome.Weather originalWeather) {
        return this.config == config
            && this.originalWeather == originalWeather
            && this.originalPrecipitation == originalWeather.hasPrecipitation
            && this.originalTemperatureBits == Float.floatToRawIntBits(originalWeather.temperature)
            && this.biomeId.equals(biomeId);
    }

    public boolean hasPrecipitation(Season season) {
        return precipitation[season.ordinal()];
    }

    public float getTemperature(Season season) {
        return temperature[season.ordinal()];
    }

}
