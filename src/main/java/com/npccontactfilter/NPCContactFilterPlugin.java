package com.npccontactfilter;

import com.google.inject.Provides;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Vector;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.widgets.Widget;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@Slf4j
@PluginDescriptor(
	name = "NPC Contact Filter"
)
public class NPCContactFilterPlugin extends Plugin
{
	private static final int NPC_CONTACT_WIDGET_GROUP = 75;
	private static final int NPC_CONTACT_WIDGET_ID = 4915204;
	private static final int X_OFFSET = 67;
	private static final int Y_OFFSET = 125;
	private static final int COLUMNS = 7;

	private static  class SortNPCs implements Comparator<Widget>
	{
		public int compare(Widget a, Widget b){
			return (a.getRelativeX() / X_OFFSET + (a.getRelativeY() / Y_OFFSET) * COLUMNS) - (b.getRelativeX() / X_OFFSET + (b.getRelativeY() / Y_OFFSET) * COLUMNS);
		}
	}

	@Inject
	private Client client;

	@Inject
	private NPCContactFilterConfig config;

	@Override
	protected void startUp() throws Exception
	{
		log.info("NPC Contact Filter started!");
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("NPC Contact Filter stopped!");
	}

	private boolean shouldHide(String name)
	{
		boolean shouldKeep;
		switch (name)
		{
			case "Honest Jimmy":
				shouldKeep = config.honestJimmy();
				break;
			case "Bert the Sandman":
				shouldKeep = config.bert();
				break;
			case "Advisor Ghrim":
				shouldKeep = config.advisorGhrim();
				break;
			case "Dark Mage":
				shouldKeep = config.darkMage();
				break;
			case "Lanthus":
				shouldKeep = config.lanthus();
				break;
			case "Spria":
				shouldKeep = config.spria();
				break;
			case "Turael":
			case "Aya":
				shouldKeep = config.turael();
				break;
			case "Mazchna":
				shouldKeep = config.mazchna();
				break;
			case "Vannaka":
				shouldKeep = config.vannaka();
				break;
			case "Chaeldar":
				shouldKeep = config.chaeldar();
				break;
			case "Nieve":
			case "Steve":
				shouldKeep = config.nieve();
				break;
			case "Duradel":
			case "Kuradal":
				shouldKeep = config.duradel();
				break;
			case "Krystilia":
				shouldKeep = config.krystilia();
				break;
			case "Konar":
				shouldKeep = config.konar();
				break;
			case "Murphy":
				shouldKeep = config.murphy();
				break;
			case "Cyrisus":
				shouldKeep = config.cyrisus();
				break;
			case "Smoggy":
				shouldKeep = config.smoggy();
				break;
			case "Ginea":
				shouldKeep = config.ginea();
				break;
			case "Watson":
				shouldKeep = config.watson();
				break;
			case "Barbarian Guard":
				shouldKeep = config.barbarianGuard();
				break;
			case "Amy":
				shouldKeep = config.amy();
				break;
			case "Random":
				shouldKeep = config.random();
				break;
			default:
				shouldKeep = true;
		}
		return !shouldKeep;
	}

	private Vector<Widget> hideWidgets(Widget[] NPCWidgets)
	{
		Vector<Widget> unhidden = new Vector<>();
		Arrays.sort(NPCWidgets, new SortNPCs());

		for (Widget NPCWidget : NPCWidgets)
		{
			if (shouldHide(NPCWidget.getStaticChildren()[1].getText()))
			{
				NPCWidget.setHidden(true);
			}
			else
			{
				unhidden.add((NPCWidget));
			}
		}

		return unhidden;
	}

	private void moveWidgets(Vector<Widget> widgets)
	{
		for (int i = 0; i < widgets.size(); i++)
		{
			int x = X_OFFSET * (i % COLUMNS);
			int y = Y_OFFSET * (i / COLUMNS);
			widgets.get(i).setPos(x, y);
		}
	}

	@Subscribe
	public void onWidgetLoaded(WidgetLoaded widgetloaded)
	{
		if (widgetloaded.getGroupId() == NPC_CONTACT_WIDGET_GROUP)
		{
			Widget NPCContactWidget = client.getWidget(NPC_CONTACT_WIDGET_ID);
			if (NPCContactWidget != null)
			{
				Vector<Widget> unhidden = hideWidgets(NPCContactWidget.getStaticChildren());
				moveWidgets((unhidden));
			}
		}
	}

	@Provides
	NPCContactFilterConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(NPCContactFilterConfig.class);
	}
}
