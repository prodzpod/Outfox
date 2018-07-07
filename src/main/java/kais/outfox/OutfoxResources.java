/**
 * Copyright � 2018 Aiden Vaughn "ItsTheKais"
 *
 * This file is part of Outfox.
 *
 * The code of Outfox is free and available under the terms of the latest version of the GNU Lesser General
 * Public License. Outfox is distributed with no warranty, implied or otherwise. Outfox should have come with
 * a copy of the GNU Lesser General Public License; if not, see: <https://www.gnu.org/licenses/>
 */

package kais.outfox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.block.state.BlockStateBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.biome.Biome;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OutfoxResources {

    //TODO: these three constants must be commented out, and the three constants below them uncommented, before building!
    public static final String MODID = "outfox";
    public static final String NAME = "Outfox";
    public static final String VERSION = "$DEV$";

    //public static final String MODID = "gradle%modid";
    //public static final String NAME = "gradle%name";
    //public static final String VERSION = "gradle%version";

    private static final Logger LOGGER = LogManager.getLogger(MODID);

    private static final ResourceLocation FOX_IDLE_RL = new ResourceLocation(MODID, "fox.idle");
    private static final ResourceLocation FOX_SNIFF_RL = new ResourceLocation(MODID, "fox.sniff");
    private static final ResourceLocation FOX_HURT_RL = new ResourceLocation(MODID, "fox.hurt");
    private static final ResourceLocation FOX_DEATH_RL = new ResourceLocation(MODID, "fox.death");

    public static final SoundEvent FOX_IDLE_SND = new SoundEvent(FOX_IDLE_RL).setRegistryName(FOX_IDLE_RL);
    public static final SoundEvent FOX_SNIFF_SND = new SoundEvent(FOX_SNIFF_RL).setRegistryName(FOX_SNIFF_RL);
    public static final SoundEvent FOX_HURT_SND = new SoundEvent(FOX_HURT_RL).setRegistryName(FOX_HURT_RL);
    public static final SoundEvent FOX_DEATH_SND = new SoundEvent(FOX_DEATH_RL).setRegistryName(FOX_DEATH_RL);

    public static final SoundEvent[] FOX_SND_SET = new SoundEvent[] {FOX_IDLE_SND, FOX_SNIFF_SND, FOX_HURT_SND, FOX_DEATH_SND};

    public static final String[] DEFAULT_COMMON_BIOMES = new String[] {

        "minecraft:roofed_forest",
        "minecraft:mutated_roofed_forest"
    };
    public static final String[] DEFAULT_RARE_BIOMES = new String[] {

        "minecraft:forest",
        "minecraft:forest_hills",
        "minecraft:birch_forest",
        "minecraft:birch_forest_hills",
        "minecraft:mutated_forest",
        "minecraft:mutated_birch_forest",
        "minecraft:mutated_birch_forest_hills"
    };
    public static final String[] DEFAULT_IMMUNE_TOOLS = new String[] {

        "pick", // vanilla/common
        "shovel", // vanilla/common
        "hammer", // various
        "excavator", // ticon
        "mattock", // ticon
        "paxel", // various
        "drill", // various
        "disassembler", // mekanism
        "destructionwand" // xu2
    };
    public static final String[] DEFAULT_STATE_MATCHES = new String[] {

        "color", // vanilla/common
        "colour", // just in case!
        "type", // vanilla/common
        "variant", // vanilla/common
        "compression_level_", // xu2 compressed blocks
        "decorstates", // xu2 decoratives
        "foliage", // ticon slimegrass
        "shade" // flat-colored-blocks
    };

    public static void logInfo(String message) { LOGGER.info(message); }
    public static void logWarn(String message) { LOGGER.warn(message); }
    public static void logError(String message) { LOGGER.error(message); }

    /**
     * converts an array of String ResourceLocations into an ArrayList of Biomes for registering
     */
    public static ArrayList<Biome> stringsToBiomes(String[] input) {

        ArrayList<Biome> biomes = new ArrayList<Biome>();
        for (String i : input) {

            String[] j = i.split(":", 2);
            Biome b = Biome.REGISTRY.getObject(new ResourceLocation(j[0], j[1]));
            if (b != null) { biomes.add(b); }
        }

        return biomes;
    }

    /**
     * converts the output of BlockStateBase.toString() into a HashMap of String pairs because I'm too stupid to figure out IProperty
     */
    public static HashMap<String, String> stateStringToHashMap(IBlockState state) {

        String statestring = ((BlockStateBase)state).toString();
        HashMap<String, String> map = new HashMap<String, String>(OutfoxConfig.search.state_matches.length);
        if (!statestring.contains("[")) { return map; }

        Pattern pattern;
        Matcher matcher;

        for (String s : OutfoxConfig.search.state_matches) {

            pattern = Pattern.compile("(?:[\\[,]" + s + ".*?=)(.*?(?=[\\],]))");
            matcher = pattern.matcher(statestring);

            if (matcher.find()) {

                String g = matcher.group(1);
                if (g != null && !g.equals("")) { map.put(s, g); }
            }
        }

        return map;
    }

    /**
     * returns true if the given block ID is allowed to be searched for given the current blacklist and listmode
     */
    public static boolean checkBlockIdIsBlacklisted(String blockname) {

        return OutfoxConfig.search.search_listmode == Arrays.asList(OutfoxConfig.search.search_list).contains(blockname);
    }
}