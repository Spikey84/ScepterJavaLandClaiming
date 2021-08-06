package io.github.spikey84.scepterjavaclaiming.particles;

import com.destroystokyo.paper.ParticleBuilder;
import io.github.spikey84.scepterjavaclaiming.Claim;
import io.github.spikey84.scepterjavaclaiming.ClaimManager;
import io.github.spikey84.scepterjavaclaiming.ConfigManager;
import io.github.spikey84.scepterjavaclaiming.utils.Rectangle;
import io.github.spikey84.scepterjavaclaiming.utils.SchedulerUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;


public class ParticleManager {
    private ClaimManager claimManager;
    private ConfigManager configManager;
    private static Particle particle = Particle.REDSTONE;

    public ParticleManager(ClaimManager claimManager, ConfigManager configManager) {
        this.claimManager = claimManager;
        this.configManager = configManager;

        SchedulerUtils.runRepeating(() -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                spawnParticlesInRange(player);
            }
        }, 20);
    }

    public void spawnParticlesInRange(Player player) {
        Claim[] claims = claimManager.getClaims().toArray(new Claim[]{});
        for (int j = 0; j < claims.length; j++) {
            Claim claim = claims[j];
            boolean own = claim.getOwner().equals(player.getUniqueId());
            if (player.getInventory().getItemInMainHand().isSimilar(configManager.getClaimChecker()) || player.getInventory().getItemInMainHand().isSimilar(configManager.getClaimTool())) {
                Location loc = claim.getOrigin().clone();
                loc.setY(player.getLocation().getY() + 1);
                pY(loc, player);

                if (!loc.getWorld().equals(player.getWorld())) continue;

                for (double x = 0; x < claim.getXLength() + 1; x++) {
                    loc.setX(loc.getX() + 1);
                    if (player.getLocation().distance(loc) < 50) {
                        if (!own) pY(loc, player); else pG(loc, player);
                    }
                }

                for (double x = 0; x < claim.getZLength() + 1; x++) {
                    loc.setZ(loc.getZ() + 1);
                    if (player.getLocation().distance(loc) < 50) {
                        if (!own) pY(loc, player); else pG(loc, player);
                    }
                }

                for (double x = 0; x < claim.getXLength() + 1; x++) {
                    loc.setX(loc.getX() - 1);
                    if (player.getLocation().distance(loc) < 50) {
                        if (!own) pY(loc, player); else pG(loc, player);
                    }
                }

                for (double x = 0; x < claim.getZLength() + 1; x++) {
                    loc.setZ(loc.getZ() - 1);
                    if (player.getLocation().distance(loc) < 50) {
                        if (!own) pY(loc, player); else pG(loc, player);
                    }
                }
            }
        }

        if (player.getInventory().getItemInMainHand().isSimilar(configManager.getClaimTool())) {
            claimManager.getTempClaiming().putIfAbsent(player.getUniqueId(), new Rectangle(null, null));

            if (claimManager.getTempClaiming().get(player.getUniqueId()).getLocation1() != null) {

                Location loc = claimManager.getTempClaiming().get(player.getUniqueId()).getLocation1().clone();
                if (loc.getWorld().equals(player.getWorld())) {
                    if (player.getLocation().distance(loc) < 50) {

                        loc.setY(player.getLocation().getY() - 2);
                        loc.setX(loc.getX() + 1);
                        loc.setZ(loc.getZ() + 0.5);
                        double permX = loc.getX();
                        double permZ = loc.getZ();

                        for (int x = 0; x < 3; x++) {
                            loc.setZ(permZ);
                            loc.setX(permX);

                            loc.setY(loc.getY() + 1);
                            loc.setZ(loc.getZ() + 0.5);

                            pR(loc, player);
                            loc.setX(loc.getX() - 1);
                            pR(loc, player);
                            loc.setZ(loc.getZ() - 1);
                            pR(loc, player);
                            loc.setX(loc.getX() + 1);
                            pR(loc, player);

                        }
                    }
                }
            }

            if (claimManager.getTempClaiming().get(player.getUniqueId()).getLocation2() != null) {
                Location loc = claimManager.getTempClaiming().get(player.getUniqueId()).getLocation2().clone();
                if (loc.getWorld().equals(player.getWorld())) {
                    if (player.getLocation().distance(loc) < 50) {
                        loc.setY(player.getLocation().getY() - 2);
                        loc.setX(loc.getX() + 1);
                        loc.setZ(loc.getZ() + 0.5);
                        double permX = loc.getX();
                        double permZ = loc.getZ();

                        for (int x = 0; x < 3; x++) {
                            loc.setZ(permZ);
                            loc.setX(permX);

                            loc.setY(loc.getY() + 1);
                            loc.setZ(loc.getZ() + 0.5);

                            pB(loc, player);
                            loc.setX(loc.getX() - 1);
                            pB(loc, player);
                            loc.setZ(loc.getZ() - 1);
                            pB(loc, player);
                            loc.setX(loc.getX() + 1);
                            pB(loc, player);

                        }
                    }
                }
            }
        }
    }

    public void pY(Location location, Player player) {
        ParticleBuilder particleBuilder = new ParticleBuilder(particle);
        particleBuilder.color(255, 255, 0);
        player.spawnParticle(particle, location,1, particleBuilder.data());
    }

    public void pG(Location location, Player player) {
        ParticleBuilder particleBuilder = new ParticleBuilder(particle);
        particleBuilder.color(0, 255, 0);
        player.spawnParticle(particle, location,1, particleBuilder.data());
    }

    public void pR(Location location, Player player) {
        ParticleBuilder particleBuilder = new ParticleBuilder(particle);
        particleBuilder.color(255, 0, 0);
        player.spawnParticle(particle, location,1, particleBuilder.data());
    }

    public void pB(Location location, Player player) {
        ParticleBuilder particleBuilder = new ParticleBuilder(particle);
        particleBuilder.color(0, 0, 255);
        player.spawnParticle(particle, location,1, particleBuilder.data());
    }
}
