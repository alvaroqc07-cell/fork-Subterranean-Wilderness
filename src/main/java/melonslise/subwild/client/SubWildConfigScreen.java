package melonslise.subwild.client;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import melonslise.subwild.common.config.SubWildConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraftforge.common.ForgeConfigSpec;

public class SubWildConfigScreen extends Screen
{
	private static final Component TITLE = Component.translatable("config.subwild.title");
	private static final Component VALUE_ON = Component.translatable("options.on").copy().withStyle(ChatFormatting.GREEN);
	private static final Component VALUE_OFF = Component.translatable("options.off").copy().withStyle(ChatFormatting.RED);
	private static final Component GLOBAL_REPLACE_TITLE = Component.literal("Replace All Cave Types");
	private static final Component GLOBAL_REPLACE_DEEP_TITLE = Component.literal("Extend to Bedrock");
	private static final Component GLOBAL_REPLACE_CHOOSER = Component.literal("Choose Global Replacement Cave");
	private static final Component SEARCH_HINT = Component.literal("Click to search settings...");
	private static final Component RESET_BUTTON = Component.literal("Reset Settings");
	private static final Component RESET_CONFIRM_BUTTON = Component.literal("Confirm?");
	private static final int TITLE_Y = 8;
	private static final int SEARCH_BOX_Y = 22;
	private static final int FIRST_ROW_Y = 50;
	private static final int SETTINGS_LIST_ROW_HEIGHT = 26;
	private static final int TOOLTIP_WIDTH = 220;
	private static final int ENTRY_WIDGET_Y = 3;
	private static final int ENTRY_LABEL_Y = 8;
	private static final int NESTED_LABEL_INDENT = 12;

	private final Screen parent;
	private final List<SubWildConfig.CaveTypeChoice> caveTypeChoices;
	private final Map<ResourceLocation, SubWildConfig.CaveTypeChoice> caveTypeChoiceLookup;
	private final List<ConfigSection> normalSections;
	private final List<FeatureSection> featureSections;
	private final GlobalReplacementState globalReplacementState;
	private final List<CaveTypeState> caveTypeStates;
	private UnifiedSettingsList settingsList;
	private EditBox searchBox;
	private Button resetButton;
	private boolean resetPending;
	private boolean refocusSearchAfterRebuild;
	private Component errorMessage;
	private int widgetWidth;
	private int footerTopY;
	private int footerButtonWidth;
	private double settingsListScrollAmount;
	private TooltipRequest tooltipRequest;
	private String searchQuery = "";

	public SubWildConfigScreen(Screen parent)
	{
		super(TITLE);
		this.parent = parent;
		this.caveTypeChoices = SubWildConfig.CAVE_TYPE_CHOICES;
		this.caveTypeChoiceLookup = this.createChoiceLookup();
		this.globalReplacementState = new GlobalReplacementState(
			SubWildConfig.GLOBAL_REPLACE_ALL_CAVE_TYPES.get(),
			SubWildConfig.GLOBAL_REPLACE_DEEP_TUFF_CAVES.get(),
			this.parseChoice(SubWildConfig.GLOBAL_REPLACEMENT_CAVE_TYPE.get(), this.getDefaultChoiceId()));
		this.caveTypeStates = this.createCaveTypeStates();
		this.featureSections = this.createFeatureSections();
		this.normalSections = this.createNormalSections();
	}

	private Map<ResourceLocation, SubWildConfig.CaveTypeChoice> createChoiceLookup()
	{
		Map<ResourceLocation, SubWildConfig.CaveTypeChoice> lookup = new LinkedHashMap<>(this.caveTypeChoices.size());
		for(SubWildConfig.CaveTypeChoice choice : this.caveTypeChoices)
			lookup.put(choice.id(), choice);
		return lookup;
	}

	private ResourceLocation getDefaultChoiceId()
	{
		return this.caveTypeChoices.isEmpty() ? new ResourceLocation("subwild", "basic") : this.caveTypeChoices.get(0).id();
	}

	private ResourceLocation parseChoice(String value, ResourceLocation fallback)
	{
		try
		{
			ResourceLocation id = new ResourceLocation(value);
			return this.caveTypeChoiceLookup.containsKey(id) ? id : fallback;
		}
		catch(IllegalArgumentException ex)
		{
			return fallback;
		}
	}

	private List<CaveTypeState> createCaveTypeStates()
	{
		List<CaveTypeState> states = new ArrayList<>(SubWildConfig.CAVE_TYPE_OPTIONS.size());
		for(SubWildConfig.CaveTypeToggle toggle : SubWildConfig.CAVE_TYPE_OPTIONS)
			states.add(new CaveTypeState(
				toggle.id(),
				toggle.label(),
				toggle.description(),
				toggle.mode().get(),
				toggle.affectDeepTuff().get(),
				this.parseChoice(toggle.replacementTarget().get(), toggle.id())));
		return List.copyOf(states);
	}

	private List<ConfigSection> createNormalSections()
	{
		List<ConfigSection> sections = new ArrayList<>();
		sections.add(new ConfigSection("General", List.of(
			this.boolOption("Expensive Scan", "Scans additional underground cave spaces to improve terrain generation compatibility at a lower performance cost (may drastically reduce cave feature generation in large caves).", SubWildConfig.EXPENSIVE_SCAN),
			this.boolOption("Adapt Cave Biomes to Modded Biomes", "Adapts SubWild cave selection to the biome above the cave so modded biomes can use matching cave themes. Turn this off to use the older tagged-biome behavior and rocky fallback.", SubWildConfig.ADAPT_CAVE_BIOMES_TO_MODDED_BIOMES),
			this.boolOption("Generate In Structures", "Allows SubWild cave decorations to generate inside large structures and underground complexes (excluding mineshafts).", SubWildConfig.GENERATE_IN_STRUCTURES),
			this.intOption("Slope Threshold", "The amount of open space in front of cave walls before stairs and slabs generate less often.", SubWildConfig.SLOPE_THRESHOLD, 0, 16)
		)));
		sections.add(new ConfigSection("Frequencies I", List.of(
			this.intOption("Slope Generation Chance", "Higher values increase how often stairs and slabs generate.", SubWildConfig.SLOPE_CHANCE, 0, 8),
			this.intOption("Slope Threshold Chance", "Generation chance used when inside the slope threshold.", SubWildConfig.SLOPE_THRESHOLD_CHANCE, 0, 7),
			this.doubleOption("Coal Shard Chance", "Coal shard generation chance in deep and volcanic cave variants, as a percentage.", SubWildConfig.COAL_SHARD_CHANCE, 1.00d, 100.00d),
			this.doubleOption("Rocky Pebble Chance", "Pebble generation chance in rocky caves, as a percentage.", SubWildConfig.ROCKY_BUTTONS_CHANCE, 1.00d, 100.00d),
			this.doubleOption("Sandstone Pebble Chance", "Sandstone pebble generation chance in sandy and sandy rocky caves, as a percentage.", SubWildConfig.SANDY_ROCKY_BUTTONS_CHANCE, 1.00d, 100.00d),
			this.doubleOption("Red Sandstone Pebble Chance", "Red sandstone pebble generation chance in mesa and red sandy cave variants, as a percentage.", SubWildConfig.RED_SANDSTONE_PEBBLE_CHANCE, 1.00d, 100.00d)
		)));
		sections.add(new ConfigSection("Frequencies II", List.of(
			this.doubleOption("Jungle Lily Pad Chance", "Lily pad generation chance in jungle caves, as a percentage.", SubWildConfig.LUSH_LILYPADS_CHANCE, 1.00d, 100.00d),
			this.doubleOption("Mossy Lily Pad Chance", "Lily pad generation chance in mossy caves, as a percentage.", SubWildConfig.MOSSY_LILYPADS_CHANCE, 1.00d, 100.00d),
			this.doubleOption("Mossy Rocky Lily Pad Chance", "Lily pad generation chance in mossy rocky caves, as a percentage.", SubWildConfig.MOSSY_ROCKY_LILYPADS_CHANCE, 1.00d, 100.00d),
			this.doubleOption("Water Puddle Generation Chance", "Water puddle generation chance in cave biomes that support puddles, as a percentage.", SubWildConfig.WATER_PUDDLE_GENERATION_CHANCE, 0.00d, 100.00d),
			this.doubleOption("Short Foxfire Mushroom Generation Chance", "Short foxfire mushroom generation chance, as a percentage. Higher values increase how often this variant appears when foxfires generate.", SubWildConfig.SHORT_FOXFIRE_GENERATION_CHANCE, 0.00d, 100.00d),
			this.doubleOption("Long Foxfire Mushroom Generation Chance", "Long foxfire mushroom generation chance, as a percentage. Higher values increase how often this variant appears when foxfires generate.", SubWildConfig.LONG_FOXFIRE_GENERATION_CHANCE, 0.00d, 100.00d),
			this.doubleOption("Jungle Leaves Chance", "Leaves generation chance in jungle caves, as a percentage. Default: 65 (recommended to avoid overcrowding). Max: 100.", SubWildConfig.LUSH_LEAVES_CHANCE, 0.00d, 100.00d),
			this.doubleOption("Jungle Sapling Chance", "Sapling generation chance in jungle caves, as a percentage.", SubWildConfig.LUSH_SAPLINGS_CHANCE, 1.00d, 100.00d),
			this.doubleOption("Jungle Volcanic Leaves Chance", "Leaves generation chance in jungle volcanic caves, as a percentage. Default: 65 (recommended to avoid overcrowding). Max: 100.", SubWildConfig.LUSH_VOLCANIC_LEAVES_CHANCE, 0.00d, 100.00d),
			this.doubleOption("Jungle Volcanic Sapling Chance", "Sapling generation chance in jungle volcanic caves, as a percentage.", SubWildConfig.LUSH_VOLCANIC_SAPLINGS_CHANCE, 1.00d, 100.00d),
			this.doubleOption("Mesa Dead Bush Chance", "Dead bush generation chance in mesa caves, as a percentage.", SubWildConfig.MESA_DEAD_BUSHES_CHANCE, 1.00d, 100.00d),
			this.doubleOption("Sandy Dead Bush Chance", "Dead bush generation chance in sandy caves, as a percentage.", SubWildConfig.SANDY_DEAD_BUSHES_CHANCE, 1.00d, 100.00d),
			this.doubleOption("Sandy Rocky Dead Bush Chance", "Dead bush generation chance in sandy rocky caves, as a percentage.", SubWildConfig.SANDY_ROCKY_DEAD_BUSHES_CHANCE, 1.00d, 100.00d)
		)));
		sections.add(new ConfigSection("Speleothems", List.of(
			this.doubleOption("Global Speleothem Generation Chance", "Default speleothem generation chance for all speleothem families, as a percentage. Individual entries below override this when set. Default: 67 (recommended to reduce crowding). Max: 100.", SubWildConfig.GLOBAL_SPELEOTHEM_GENERATION_CHANCE, 0.00d, 100.00d),
			this.doubleOption("Stone Generation Chance", "Stone speleothem generation chance, as a percentage. Set to -1 to use the global speleothem generation chance. Frozen stone speleothems follow this value.", SubWildConfig.STONE_SPELEOTHEM_GENERATION_CHANCE, -1.00d, 100.00d),
			this.doubleOption("Deepslate Generation Chance", "Deepslate speleothem generation chance, as a percentage. Set to -1 to use the global speleothem generation chance.", SubWildConfig.DEEPSLATE_SPELEOTHEM_GENERATION_CHANCE, -1.00d, 100.00d),
			this.doubleOption("Granite Generation Chance", "Granite speleothem generation chance, as a percentage. Set to -1 to use the global speleothem generation chance. Frozen granite speleothems follow this value.", SubWildConfig.GRANITE_SPELEOTHEM_GENERATION_CHANCE, -1.00d, 100.00d),
			this.doubleOption("Diorite Generation Chance", "Diorite speleothem generation chance, as a percentage. Set to -1 to use the global speleothem generation chance. Frozen diorite speleothems follow this value.", SubWildConfig.DIORITE_SPELEOTHEM_GENERATION_CHANCE, -1.00d, 100.00d),
			this.doubleOption("Andesite Generation Chance", "Andesite speleothem generation chance, as a percentage. Set to -1 to use the global speleothem generation chance. Frozen andesite speleothems follow this value.", SubWildConfig.ANDESITE_SPELEOTHEM_GENERATION_CHANCE, -1.00d, 100.00d),
			this.doubleOption("Sandstone Generation Chance", "Sandstone speleothem generation chance, as a percentage. Set to -1 to use the global speleothem generation chance. Frozen sandstone speleothems follow this value.", SubWildConfig.SANDSTONE_SPELEOTHEM_GENERATION_CHANCE, -1.00d, 100.00d),
			this.doubleOption("Red Sandstone Generation Chance", "Red sandstone speleothem generation chance, as a percentage. Set to -1 to use the global speleothem generation chance. Frozen red sandstone speleothems follow this value.", SubWildConfig.RED_SANDSTONE_SPELEOTHEM_GENERATION_CHANCE, -1.00d, 100.00d),
			this.doubleOption("Obsidian Generation Chance", "Obsidian speleothem generation chance, as a percentage. Set to -1 to use the global speleothem generation chance. Frozen obsidian speleothems follow this value.", SubWildConfig.OBSIDIAN_SPELEOTHEM_GENERATION_CHANCE, -1.00d, 100.00d),
			this.doubleOption("Blackstone Generation Chance", "Blackstone speleothem generation chance, as a percentage. Set to -1 to use the global speleothem generation chance. Frozen blackstone speleothems follow this value.", SubWildConfig.BLACKSTONE_SPELEOTHEM_GENERATION_CHANCE, -1.00d, 100.00d),
			this.doubleOption("Basalt Generation Chance", "Basalt speleothem generation chance, as a percentage. Set to -1 to use the global speleothem generation chance. Frozen basalt speleothems follow this value.", SubWildConfig.BASALT_SPELEOTHEM_GENERATION_CHANCE, -1.00d, 100.00d)
		)));
		return List.copyOf(sections);
	}

	private List<FeatureSection> createFeatureSections()
	{
		List<FeatureSection> sections = new ArrayList<>();
		sections.add(new FeatureSection("Features I", List.of(
			this.featureToggle("Generate Pebbles", "Enable pebble generation in rocky and sandy cave biomes.", SubWildConfig.GENERATE_BUTTONS),
			this.featureToggle("Throwable Pebbles", "Allow pebble items to be thrown as projectiles. Sneaking while aiming at a block throws the pebble instead of placing it.", SubWildConfig.THROWABLE_PEBBLES),
			this.featureToggle("Generate Vines", "Enable vines in fungal, lush, and mossy cave biomes.", SubWildConfig.GENERATE_VINES),
			this.featureToggle("Generate Puddles", "Enable puddles in lush, mossy muddy, and dead coral cave biomes.", SubWildConfig.GENERATE_PUDDLES),
			this.featureToggle("Generate Stairs", "Enable stairs in cave biomes. Slope Generation Chance must be above 0.", SubWildConfig.GENERATE_STAIRS),
			this.featureToggle("Generate Slabs", "Enable slabs in cave biomes. Slope Generation Chance must be above 0.", SubWildConfig.GENERATE_SLABS),
			this.featureToggle("Generate Patches", "Enable patch generation in cave biomes.", SubWildConfig.GENERATE_PATCHES),
			this.featureToggle("Generate Speleothems", "Enable speleothems in cave biomes.", SubWildConfig.GENERATE_SPELEOTHEMS),
			this.featureToggle("Generate Features in Water", "Allow speleothems, slabs, and stairs to generate in water-filled caves, cave lakes, and other flooded cave spaces.", SubWildConfig.GENERATE_FEATURES_IN_WATER),
			this.featureToggle("Generate Foxfires", "Enable glowing foxfires in cave biomes.", SubWildConfig.GENERATE_FOXFIRES),
			this.featureToggle("Generate SubWild Ores", "Enable all SubWild custom ore variants. When disabled, none of the mod's custom ores will generate.", SubWildConfig.GENERATE_SUBWILD_ORES)
		)));
		sections.add(new FeatureSection("Features II", List.of(
			this.featureToggle("Generate Dead Bushes", "Enable dead bushes in mesa, sandy, and sandy rocky cave biomes.", SubWildConfig.GENERATE_DEAD_BUSHES),
			this.featureToggle("Generate Lily Pads", "Enable lily pads in lush, mossy, and mossy rocky cave biomes.", SubWildConfig.GENERATE_LILYPADS),
			this.featureToggle("Generate Wall Coral", "Enable wall coral in coral cave biomes.", SubWildConfig.GENERATE_WALL_CORAL),
			this.featureToggle("Generate Dead Wall Coral", "Enable dead wall coral in dead coral cave biomes.", SubWildConfig.GENERATE_DEAD_WALL_CORAL),
			this.featureToggle("Generate Floor Coral", "Enable floor coral in coral cave biomes.", SubWildConfig.GENERATE_FLOOR_CORAL),
			this.featureToggle("Generate Dead Floor Coral", "Enable dead floor coral in dead coral cave biomes.", SubWildConfig.GENERATE_DEAD_FLOOR_CORAL),
			this.featureToggle("Generate Saplings", "Enable saplings in jungle and jungle volcanic cave biomes.", SubWildConfig.GENERATE_SAPLINGS),
			this.featureToggle("Shear Vanilla Mossy Blocks", "Allow shears to clean vanilla mossy blocks, stairs, slabs, and walls back to their clean variants while dropping a vine.", SubWildConfig.SHEAR_VANILLA_MOSSY_BLOCKS)
		)));
		return List.copyOf(sections);
	}

	@Override
	protected void init()
	{
		this.rebuildPageWidgets();
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick)
	{
		this.tooltipRequest = null;
		this.renderBackground(guiGraphics);
		super.render(guiGraphics, mouseX, mouseY, partialTick);
		if(this.settingsList == null)
			return;

		guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, TITLE_Y, 0xFFFFFF);

		if(this.errorMessage != null)
			guiGraphics.drawCenteredString(this.font, this.errorMessage, this.width / 2, this.footerTopY - this.font.lineHeight - 4, 0xFF5555);
		if(this.tooltipRequest != null)
			this.renderTooltip(guiGraphics, this.tooltipRequest);
	}

	private void renderTooltip(GuiGraphics guiGraphics, TooltipRequest request)
	{
		List<FormattedCharSequence> lines = this.font.split(request.text(), TOOLTIP_WIDTH);
		int tooltipWidth = 0;
		for(FormattedCharSequence line : lines)
			tooltipWidth = Math.max(tooltipWidth, this.font.width(line));
		int x = request.anchorX() + request.anchorWidth() + 8;
		if(x + tooltipWidth > this.width - 6)
			x = Math.max(6, request.anchorX() - tooltipWidth - 12);
		int y = Mth.clamp(request.anchorY() - 4, 6, Math.max(6, this.height - 12));
		guiGraphics.renderTooltip(this.font, lines, x, y);
	}

	private void queueTooltip(Component text, int anchorX, int anchorY, int anchorWidth)
	{
		if(this.tooltipRequest == null)
			this.tooltipRequest = new TooltipRequest(text, anchorX, anchorY, anchorWidth);
	}

	@Override
	public void onClose()
	{
		this.minecraft.setScreen(this.parent);
	}

	private void rebuildPageWidgets()
	{
		this.rebuildPageWidgets(true);
	}

	private void rebuildPageWidgets(boolean preserveScroll)
	{
		if(preserveScroll)
			this.captureScrollablePageState();
		else
			this.settingsListScrollAmount = 0.0d;
		this.clearWidgets();
		this.searchBox = null;
		this.resetButton = null;
		this.errorMessage = null;
		this.computeLayout();
		this.createSearchBox();
		this.settingsList = this.addRenderableWidget(new UnifiedSettingsList(this.minecraft, this.width, FIRST_ROW_Y, this.footerTopY - 6));
		this.settingsList.setScrollAmount(this.settingsListScrollAmount);

		int gap = 4;
		int footerRowX = (this.width - (this.footerButtonWidth * 3 + gap * 2)) / 2;
		this.resetButton = this.addRenderableWidget(Button.builder(this.resetButtonMessage(), button -> this.handleResetButton())
			.bounds(footerRowX, this.footerTopY, this.footerButtonWidth, 20).build());
		this.addRenderableWidget(Button.builder(Component.translatable("gui.done"), button -> this.saveAndClose())
			.bounds(footerRowX + this.footerButtonWidth + gap, this.footerTopY, this.footerButtonWidth, 20).build());
		this.addRenderableWidget(Button.builder(Component.translatable("gui.cancel"), button -> this.onClose())
			.bounds(footerRowX + (this.footerButtonWidth + gap) * 2, this.footerTopY, this.footerButtonWidth, 20).build());
	}

	private void createSearchBox()
	{
		int width = Math.min(220, this.width - 40);
		int x = (this.width - width) / 2;
		EditBox box = new EditBox(this.font, x, SEARCH_BOX_Y, width, 18, Component.literal("Search"));
		box.setHint(SEARCH_HINT);
		box.setMaxLength(80);
		box.setValue(this.searchQuery);
		box.setResponder(value ->
		{
			if(value.equals(this.searchQuery))
				return;
			this.searchQuery = value;
			this.refocusSearchAfterRebuild = true;
			this.rebuildPageWidgets(false);
		});
		this.searchBox = this.addRenderableWidget(box);
		if(this.refocusSearchAfterRebuild)
		{
			this.setFocused(this.searchBox);
			this.searchBox.setFocused(true);
			this.searchBox.moveCursorToEnd();
			this.refocusSearchAfterRebuild = false;
		}
	}

	private void captureScrollablePageState()
	{
		if(this.settingsList != null)
			this.settingsListScrollAmount = this.settingsList.getScrollAmount();
	}

	private boolean matchesSearch(String... values)
	{
		return matchesQuery(this.searchQuery, values);
	}

	private static boolean matchesQuery(String query, String... values)
	{
		String normalizedQuery = query == null ? "" : query.trim().toLowerCase();
		if(normalizedQuery.isEmpty())
			return true;

		StringBuilder builder = new StringBuilder();
		for(String value : values)
		{
			if(value == null || value.isBlank())
				continue;
			if(builder.length() > 0)
				builder.append(' ');
			builder.append(value.toLowerCase());
		}
		String haystack = builder.toString();
		for(String token : normalizedQuery.split("\\s+"))
			if(!haystack.contains(token))
				return false;
		return true;
	}

	private Component globalReplacementLabel()
	{
		return Component.literal("Mode: ").append(this.globalReplacementState.enabled ? VALUE_ON : VALUE_OFF);
	}

	private Component resetButtonMessage()
	{
		return this.resetPending ? RESET_CONFIRM_BUTTON : RESET_BUTTON;
	}

	private void refreshResetButton()
	{
		if(this.resetButton != null)
			this.resetButton.setMessage(this.resetButtonMessage());
	}

	private void cancelResetConfirmation()
	{
		if(!this.resetPending)
			return;
		this.resetPending = false;
		this.refreshResetButton();
	}

	private void saveAndClose()
	{
		try
		{
			this.cancelResetConfirmation();
			this.saveChanges();
			this.onClose();
		}
		catch(IllegalArgumentException ex)
		{
			this.errorMessage = Component.literal(ex.getMessage());
		}
	}

	private void saveChanges()
	{
		for(ConfigSection section : this.normalSections)
			for(ConfigOption option : section.options())
				option.save();
		this.saveFeatureStates();
		this.saveCaveTypeStates();
	}

	private void saveFeatureStates()
	{
		for(FeatureSection section : this.featureSections)
			for(FeatureToggleState state : section.options())
				state.save();
	}

	private void saveCaveTypeStates()
	{
		SubWildConfig.GLOBAL_REPLACE_ALL_CAVE_TYPES.set(this.globalReplacementState.enabled);
		SubWildConfig.GLOBAL_REPLACE_ALL_CAVE_TYPES.save();
		SubWildConfig.GLOBAL_REPLACE_DEEP_TUFF_CAVES.set(this.globalReplacementState.affectDeepTuffCaves);
		SubWildConfig.GLOBAL_REPLACE_DEEP_TUFF_CAVES.save();
		SubWildConfig.GLOBAL_REPLACEMENT_CAVE_TYPE.set(this.globalReplacementState.target.toString());
		SubWildConfig.GLOBAL_REPLACEMENT_CAVE_TYPE.save();

		for(CaveTypeState state : this.caveTypeStates)
		{
			SubWildConfig.CaveTypeToggle toggle = SubWildConfig.CAVE_TYPE_TOGGLES.get(state.id);
			if(toggle == null)
				continue;
			toggle.mode().set(state.mode);
			toggle.mode().save();
			toggle.affectDeepTuff().set(state.affectDeepTuffCaves);
			toggle.affectDeepTuff().save();
			toggle.replacementTarget().set(state.replacementTarget.toString());
			toggle.replacementTarget().save();
		}
	}

	private void handleResetButton()
	{
		if(!this.resetPending)
		{
			this.resetPending = true;
			this.refreshResetButton();
			return;
		}

		this.resetSubWildConfig();
	}

	private void resetSubWildConfig()
	{
		this.errorMessage = null;
		SubWildConfig.resetToDefaults();
		SubWildConfig.SPEC.afterReload();

		SubWildConfigScreen refreshed = new SubWildConfigScreen(this.parent);
		refreshed.settingsListScrollAmount = this.settingsListScrollAmount;
		this.minecraft.setScreen(refreshed);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button)
	{
		if(this.resetPending && (this.resetButton == null || !this.resetButton.isMouseOver(mouseX, mouseY)))
			this.cancelResetConfirmation();
		this.clearFocusedInputIfClickedOutside(mouseX, mouseY);
		return super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double delta)
	{
		this.cancelResetConfirmation();
		this.clearFocusedInput();
		return super.mouseScrolled(mouseX, mouseY, delta);
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers)
	{
		if(this.resetPending)
			this.cancelResetConfirmation();
		return super.keyPressed(keyCode, scanCode, modifiers);
	}

	@Override
	public boolean charTyped(char codePoint, int modifiers)
	{
		if(this.resetPending)
			this.cancelResetConfirmation();
		return super.charTyped(codePoint, modifiers);
	}

	private void clearFocusedInputIfClickedOutside(double mouseX, double mouseY)
	{
		if(this.settingsList == null)
			return;
		if(this.searchBox != null && this.searchBox.isFocused() && this.searchBox.isMouseOver(mouseX, mouseY))
			return;

		for(ConfigListEntry entry : this.settingsList.children())
		{
			if(!(entry instanceof ConfigOptionEntry optionEntry) || !(optionEntry.option.widget instanceof EditBox editBox) || !editBox.isFocused())
				continue;
			if(this.isMouseOverEditBox(editBox, mouseX, mouseY))
				return;
		}

		this.clearFocusedInput();
	}

	private boolean isMouseOverEditBox(EditBox editBox, double mouseX, double mouseY)
	{
		return mouseX >= editBox.getX() && mouseX < editBox.getX() + editBox.getWidth() && mouseY >= editBox.getY() && mouseY < editBox.getY() + editBox.getHeight();
	}

	private void clearFocusedInput()
	{
		if(this.searchBox != null)
			this.searchBox.setFocused(false);
		if(this.settingsList == null)
			return;

		for(ConfigListEntry entry : this.settingsList.children())
		{
			if(entry instanceof ConfigOptionEntry optionEntry && optionEntry.option.widget instanceof EditBox editBox && editBox.isFocused())
			{
				if(optionEntry.option instanceof TextOption textOption)
					textOption.onFocusLost();
				editBox.setFocused(false);
			}
		}
		this.settingsList.setFocused(null);
		this.setFocused((GuiEventListener) null);
	}

	private Component choiceLabel(ResourceLocation id)
	{
		SubWildConfig.CaveTypeChoice choice = this.caveTypeChoiceLookup.get(id);
		return Component.literal(choice != null ? choice.label() : id.toString());
	}

	private String choiceText(ResourceLocation id)
	{
		SubWildConfig.CaveTypeChoice choice = this.caveTypeChoiceLookup.get(id);
		return choice != null ? choice.label() : id.toString();
	}

	private int caveTypeColor(ResourceLocation id)
	{
		String path = id == null ? "" : id.getPath();
		if(path.contains("icy"))
			return 0x9EDFEA;
		if(path.contains("fungal"))
			return 0xB48CE0;
		if(path.contains("muddy"))
			return 0x7A8747;
		if(path.contains("mossy") || path.contains("lush") || path.contains("podzol"))
			return 0x78BF6A;
		if(path.contains("sandy_red") || path.contains("mesa"))
			return 0xD37A57;
		if(path.contains("volcanic"))
			return path.contains("red") ? 0xD66B3D : 0xE0913E;
		if(path.contains("sandy"))
			return 0xD7C17A;
		if(path.contains("dead_coral"))
			return 0xB88E79;
		if(path.contains("coral"))
			return 0x65C4BF;
		if(path.contains("rocky") || path.contains("basic"))
			return 0xBBBBBB;
		return 0xFFFFFF;
	}

	private void drawCaveTypeReference(GuiGraphics guiGraphics, Font font, String prefix, ResourceLocation id, int x, int y, int maxWidth)
	{
		String label = this.choiceText(id);
		int prefixWidth = font.width(prefix);
		int labelWidth = Math.max(0, maxWidth - prefixWidth);
		String trimmedLabel = font.plainSubstrByWidth(label, Math.max(12, labelWidth));
		if(trimmedLabel.length() < label.length() && font.width(trimmedLabel + "...") <= labelWidth)
			trimmedLabel = trimmedLabel + "...";
		guiGraphics.drawString(font, prefix, x, y, 0xA0A0A0, false);
		guiGraphics.drawString(font, trimmedLabel, x + prefixWidth, y, this.caveTypeColor(id), false);
	}

	private Component caveModeLabel(SubWildConfig.CaveTypeMode mode)
	{
		return switch(mode)
		{
		case ON -> Component.literal("ON").withStyle(ChatFormatting.GREEN);
		case LEAVE_VANILLA -> Component.literal("OFF (Leave as Vanilla)").withStyle(ChatFormatting.RED);
		case REPLACE_WITH_SUBWILD -> Component.literal("Replace with SubWild").withStyle(ChatFormatting.GOLD);
		};
	}

	private SubWildConfig.CaveTypeMode nextMode(SubWildConfig.CaveTypeMode mode)
	{
		return switch(mode)
		{
		case ON -> SubWildConfig.CaveTypeMode.LEAVE_VANILLA;
		case LEAVE_VANILLA -> SubWildConfig.CaveTypeMode.REPLACE_WITH_SUBWILD;
		case REPLACE_WITH_SUBWILD -> SubWildConfig.CaveTypeMode.ON;
		};
	}

	private void openCaveTypeSelection(String title, ResourceLocation currentSelection, Consumer<ResourceLocation> onSelected)
	{
		this.minecraft.setScreen(new CaveTypeSelectionScreen(this, Component.literal(title), currentSelection, onSelected));
	}

	private void computeLayout()
	{
		int margin = Math.max(10, Math.min(20, this.width / 24));
		int contentWidth = Math.max(140, this.width - margin * 2);

		this.widgetWidth = Math.max(72, Math.min(190, contentWidth * 2 / 5));
		this.footerButtonWidth = Math.max(90, Math.min(130, (contentWidth - 8) / 3));
		this.footerTopY = this.height - margin - 20;
	}

	private BoolOption boolOption(String label, String description, ForgeConfigSpec.BooleanValue value)
	{
		return new BoolOption(Component.literal(label), Component.literal(description), value.get(), newValue ->
		{
			value.set(newValue);
			value.save();
		});
	}

	private FeatureToggleState featureToggle(String label, String description, ForgeConfigSpec.BooleanValue value)
	{
		return new FeatureToggleState(Component.literal(label), Component.literal(description), value.get(), newValue ->
		{
			value.set(newValue);
			value.save();
		});
	}

	private IntOption intOption(String label, String description, ForgeConfigSpec.IntValue value, int min, int max)
	{
		return new IntOption(Component.literal(label), Component.literal(description), value, min, max);
	}

	private DoubleOption doubleOption(String label, String description, ForgeConfigSpec.DoubleValue value, double min, double max)
	{
		return new DoubleOption(Component.literal(label), Component.literal(description), value, min, max);
	}

	private record ConfigSection(String title, List<ConfigOption> options) {}

	private record TooltipRequest(Component text, int anchorX, int anchorY, int anchorWidth) {}

	private abstract static class ConfigOption
	{
		protected final Component label;
		protected final Component description;
		protected AbstractWidget widget;

		protected ConfigOption(Component label, Component description)
		{
			this.label = label;
			this.description = description;
		}

		protected abstract AbstractWidget createWidget(SubWildConfigScreen screen, int x, int y, int width);

		public abstract void save();
	}

	private static final class BoolOption extends ConfigOption
	{
		private final Consumer<Boolean> saver;
		private boolean currentValue;

		private BoolOption(Component label, Component description, boolean initialValue, Consumer<Boolean> saver)
		{
			super(label, description);
			this.saver = saver;
			this.currentValue = initialValue;
		}

		@Override
		protected AbstractWidget createWidget(SubWildConfigScreen screen, int x, int y, int width)
		{
			return Button.builder(this.currentValue ? VALUE_ON : VALUE_OFF, button ->
			{
				this.currentValue = !this.currentValue;
				button.setMessage(this.currentValue ? VALUE_ON : VALUE_OFF);
			}).bounds(x, y, width, 20).build();
		}

		@Override
		public void save()
		{
			this.saver.accept(this.currentValue);
		}
	}

	private abstract static class TextOption extends ConfigOption
	{
		protected String text;

		protected TextOption(Component label, Component description, String initialValue)
		{
			super(label, description);
			this.text = initialValue;
		}

		@Override
		protected AbstractWidget createWidget(SubWildConfigScreen screen, int x, int y, int width)
		{
			EditBox box = new EditBox(screen.font, x, y, width, 20, this.label);
			box.setMaxLength(256);
			box.setValue(this.text);
			box.setResponder(newValue -> this.text = newValue);
			return box;
		}

		protected void updateText(String value)
		{
			this.text = value;
			if(this.widget instanceof EditBox editBox)
				editBox.setValue(value);
		}

		protected void onFocusLost() {}
	}

	private static final class IntOption extends TextOption
	{
		private final ForgeConfigSpec.IntValue value;
		private final int min;
		private final int max;

		private IntOption(Component label, Component description, ForgeConfigSpec.IntValue value, int min, int max)
		{
			super(label, description, Integer.toString(value.get()));
			this.value = value;
			this.min = min;
			this.max = max;
		}

		@Override
		public void save()
		{
			try
			{
				int parsed = Integer.parseInt(this.text.trim());
				parsed = Mth.clamp(parsed, this.min, this.max);
				this.updateText(Integer.toString(parsed));
				this.value.set(parsed);
				this.value.save();
			}
			catch(NumberFormatException ex)
			{
				throw new IllegalArgumentException(this.label.getString() + " must be a whole number.");
			}
		}

		@Override
		protected void onFocusLost()
		{
			try
			{
				int parsed = Integer.parseInt(this.text.trim());
				this.updateText(Integer.toString(Mth.clamp(parsed, this.min, this.max)));
			}
			catch(NumberFormatException ex)
			{
			}
		}
	}

	private static final class DoubleOption extends TextOption
	{
		private final ForgeConfigSpec.DoubleValue value;
		private final double min;
		private final double max;

		private DoubleOption(Component label, Component description, ForgeConfigSpec.DoubleValue value, double min, double max)
		{
			super(label, description, Double.toString(value.get()));
			this.value = value;
			this.min = min;
			this.max = max;
		}

		@Override
		public void save()
		{
			try
			{
				double parsed = Double.parseDouble(this.text.trim());
				parsed = Mth.clamp(parsed, this.min, this.max);
				this.updateText(Double.toString(parsed));
				this.value.set(parsed);
				this.value.save();
			}
			catch(NumberFormatException ex)
			{
				throw new IllegalArgumentException(this.label.getString() + " must be a number.");
			}
		}

		@Override
		protected void onFocusLost()
		{
			try
			{
				double parsed = Double.parseDouble(this.text.trim());
				this.updateText(Double.toString(Mth.clamp(parsed, this.min, this.max)));
			}
			catch(NumberFormatException ex)
			{
			}
		}
	}

	private abstract class ConfigListEntry extends ObjectSelectionList.Entry<ConfigListEntry> {}

	private final class UnifiedSettingsList extends ObjectSelectionList<ConfigListEntry>
	{
		private UnifiedSettingsList(Minecraft minecraft, int width, int top, int bottom)
		{
			super(minecraft, width, SubWildConfigScreen.this.height, top, bottom, SETTINGS_LIST_ROW_HEIGHT);
			this.addEntries();
		}

		private void addEntries()
		{
			for(int index = 0; index < SubWildConfigScreen.this.normalSections.size(); ++index)
			{
				if(index == 0)
					this.addConfigSection(SubWildConfigScreen.this.normalSections.get(index));
			}

			boolean showGlobalGroup = SubWildConfigScreen.this.matchesSearch(
				"Cave Types",
				GLOBAL_REPLACE_TITLE.getString(),
				GLOBAL_REPLACE_DEEP_TITLE.getString(),
				GLOBAL_REPLACE_CHOOSER.getString(),
				SubWildConfigScreen.this.choiceLabel(SubWildConfigScreen.this.globalReplacementState.target).getString(),
				"selected replacement cave global");
			List<CaveTypeState> matchingCaveTypes = new ArrayList<>();
			for(CaveTypeState state : SubWildConfigScreen.this.caveTypeStates)
			{
				if(SubWildConfigScreen.this.matchesSearch(
					state.label,
					state.description,
					SubWildConfigScreen.this.caveModeLabel(state.mode).getString(),
					GLOBAL_REPLACE_DEEP_TITLE.getString(),
					"replacement replace subwild",
					SubWildConfigScreen.this.choiceLabel(state.replacementTarget).getString()))
					matchingCaveTypes.add(state);
			}
			if(showGlobalGroup || !matchingCaveTypes.isEmpty())
				this.addEntry(new SectionEntry("Cave Types"));
			if(showGlobalGroup)
			{
				this.addEntry(new GlobalReplacementEntry());
				if(SubWildConfigScreen.this.globalReplacementState.enabled)
				{
					this.addEntry(new GlobalReplacementDeepToggleEntry());
					this.addEntry(new GlobalReplacementSelectionEntry());
					this.addEntry(new SpacerEntry());
				}
			}
			for(CaveTypeState state : matchingCaveTypes)
			{
				this.addEntry(new CaveTypeEntry(state));
				if(state.mode == SubWildConfig.CaveTypeMode.REPLACE_WITH_SUBWILD)
				{
					this.addEntry(new CaveTypeDeepToggleEntry(state));
					this.addEntry(new CaveTypeReplacementEntry(state));
					this.addEntry(new SpacerEntry());
				}
			}
			for(FeatureSection section : SubWildConfigScreen.this.featureSections)
			{
				List<FeatureToggleState> matchingFeatures = new ArrayList<>();
				for(FeatureToggleState state : section.options())
					if(SubWildConfigScreen.this.matchesSearch(state.label().getString(), state.description().getString(), state.message().getString()))
						matchingFeatures.add(state);
				if(matchingFeatures.isEmpty())
					continue;
				this.addEntry(new SectionEntry(section.title()));
				for(FeatureToggleState state : matchingFeatures)
					this.addEntry(new FeatureToggleEntry(state));
			}
			for(int index = 1; index < SubWildConfigScreen.this.normalSections.size(); ++index)
				this.addConfigSection(SubWildConfigScreen.this.normalSections.get(index));
		}

		private void addConfigSection(ConfigSection section)
		{
			List<ConfigOption> matchingOptions = new ArrayList<>();
			for(ConfigOption option : section.options())
				if(SubWildConfigScreen.this.matchesSearch(option.label.getString(), option.description.getString()))
					matchingOptions.add(option);
			if(matchingOptions.isEmpty())
				return;
			this.addEntry(new SectionEntry(section.title()));
			for(ConfigOption option : matchingOptions)
				this.addEntry(new ConfigOptionEntry(option));
		}

		@Override
		public int getRowWidth()
		{
			return Math.min(420, SubWildConfigScreen.this.width - 48);
		}

		@Override
		protected int getScrollbarPosition()
		{
			return this.getRowRight() + 8;
		}

		@Override
		protected void renderSelection(GuiGraphics guiGraphics, int top, int width, int height, int outerColor, int innerColor)
		{
		}
	}

	private final class SectionEntry extends ConfigListEntry
	{
		private final String title;

		private SectionEntry(String title)
		{
			this.title = title;
		}

		@Override
		public void render(GuiGraphics guiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovered, float partialTick)
		{
			guiGraphics.drawString(SubWildConfigScreen.this.font, this.title, left + 4, top + ENTRY_LABEL_Y, 0xA0A0A0, false);
		}

		public List<? extends GuiEventListener> children()
		{
			return List.of();
		}

		@Override
		public Component getNarration()
		{
			return Component.literal(this.title);
		}
	}

	private final class SpacerEntry extends ConfigListEntry
	{
		@Override
		public void render(GuiGraphics guiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovered, float partialTick)
		{
		}

		public List<? extends GuiEventListener> children()
		{
			return List.of();
		}

		@Override
		public Component getNarration()
		{
			return Component.empty();
		}
	}

	private final class ConfigOptionEntry extends ConfigListEntry
	{
		private final ConfigOption option;

		private ConfigOptionEntry(ConfigOption option)
		{
			this.option = option;
			this.option.widget = this.option.createWidget(SubWildConfigScreen.this, 0, 0, Math.max(96, SubWildConfigScreen.this.widgetWidth));
		}

		@Override
		public void render(GuiGraphics guiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovered, float partialTick)
		{
			int buttonWidth = Math.min(160, Math.max(96, SubWildConfigScreen.this.widgetWidth));
			int buttonX = left + width - buttonWidth - 6;
			int labelRight = buttonX - 8;
			String text = this.option.label.getString();
			String trimmed = SubWildConfigScreen.this.font.plainSubstrByWidth(text, Math.max(40, labelRight - left));
			if(trimmed.length() < text.length())
				trimmed = trimmed + "...";
			guiGraphics.drawString(SubWildConfigScreen.this.font, trimmed, left + 4, top + ENTRY_LABEL_Y, 0xFFFFFF, false);

			this.option.widget.setX(buttonX);
			this.option.widget.setY(top + ENTRY_WIDGET_Y);
			this.option.widget.setWidth(buttonWidth);
			this.option.widget.render(guiGraphics, mouseX, mouseY, partialTick);

			if(this.option.widget.isMouseOver(mouseX, mouseY) || mouseX >= left + 4 && mouseX < labelRight && mouseY >= top && mouseY < top + 20)
				SubWildConfigScreen.this.queueTooltip(this.option.description, this.option.widget.getX(), this.option.widget.getY(), this.option.widget.getWidth());
		}

		public List<? extends GuiEventListener> children()
		{
			return List.of(this.option.widget);
		}

		@Override
		public boolean mouseClicked(double mouseX, double mouseY, int button)
		{
			boolean clicked = this.option.widget.mouseClicked(mouseX, mouseY, button);
			if(this.option.widget instanceof EditBox editBox)
			{
				editBox.setFocused(clicked);
				if(clicked && SubWildConfigScreen.this.settingsList != null)
					SubWildConfigScreen.this.settingsList.setFocused(this);
			}
			return clicked;
		}

		@Override
		public boolean mouseReleased(double mouseX, double mouseY, int button)
		{
			return this.option.widget.mouseReleased(mouseX, mouseY, button);
		}

		@Override
		public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY)
		{
			return this.option.widget.mouseDragged(mouseX, mouseY, button, dragX, dragY);
		}

		@Override
		public boolean keyPressed(int keyCode, int scanCode, int modifiers)
		{
			return this.option.widget.isFocused() && this.option.widget.keyPressed(keyCode, scanCode, modifiers);
		}

		@Override
		public boolean charTyped(char codePoint, int modifiers)
		{
			return this.option.widget.isFocused() && this.option.widget.charTyped(codePoint, modifiers);
		}

		@Override
		public Component getNarration()
		{
			return this.option.label;
		}
	}

	private final class GlobalReplacementEntry extends ConfigListEntry
	{
		private final Button modeButton;

		private GlobalReplacementEntry()
		{
			this.modeButton = Button.builder(SubWildConfigScreen.this.globalReplacementLabel(), button ->
			{
				SubWildConfigScreen.this.globalReplacementState.enabled = !SubWildConfigScreen.this.globalReplacementState.enabled;
				SubWildConfigScreen.this.rebuildPageWidgets();
			}).bounds(0, 0, 150, 20).build();
		}

		@Override
		public void render(GuiGraphics guiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovered, float partialTick)
		{
			int buttonWidth = 150;
			int buttonX = left + width - buttonWidth - 6;
			int labelRight = buttonX - 8;
			guiGraphics.drawString(SubWildConfigScreen.this.font, GLOBAL_REPLACE_TITLE, left + 4, top + ENTRY_LABEL_Y, 0xFFFFFF, false);

			this.modeButton.setX(buttonX);
			this.modeButton.setY(top + ENTRY_WIDGET_Y);
			this.modeButton.setMessage(SubWildConfigScreen.this.globalReplacementLabel());
			this.modeButton.render(guiGraphics, mouseX, mouseY, partialTick);

			if(this.modeButton.isMouseOver(mouseX, mouseY) || mouseX >= left + 4 && mouseX < labelRight && mouseY >= top && mouseY < top + 20)
				SubWildConfigScreen.this.queueTooltip(Component.literal("Replace every SubWild cave type with one selected cave type. This overrides biome-based cave selection."), this.modeButton.getX(), this.modeButton.getY(), this.modeButton.getWidth());
		}

		public List<? extends GuiEventListener> children()
		{
			return List.of(this.modeButton);
		}

		@Override
		public boolean mouseClicked(double mouseX, double mouseY, int button)
		{
			return this.modeButton.mouseClicked(mouseX, mouseY, button);
		}

		@Override
		public boolean mouseReleased(double mouseX, double mouseY, int button)
		{
			return this.modeButton.mouseReleased(mouseX, mouseY, button);
		}

		@Override
		public Component getNarration()
		{
			return GLOBAL_REPLACE_TITLE;
		}
	}

	private final class GlobalReplacementSelectionEntry extends ConfigListEntry
	{
		private final Button chooseButton;

		private GlobalReplacementSelectionEntry()
		{
			this.chooseButton = Button.builder(GLOBAL_REPLACE_CHOOSER, button ->
				SubWildConfigScreen.this.openCaveTypeSelection("Select Global Replacement Cave", SubWildConfigScreen.this.globalReplacementState.target, selected ->
					SubWildConfigScreen.this.globalReplacementState.target = selected)).bounds(0, 0, 150, 20).build();
		}

		@Override
		public void render(GuiGraphics guiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovered, float partialTick)
		{
			int buttonWidth = 150;
			int buttonX = left + width - buttonWidth - 6;
			int labelRight = buttonX - 8;
			SubWildConfigScreen.this.drawCaveTypeReference(guiGraphics, SubWildConfigScreen.this.font, "Selected: ",
				SubWildConfigScreen.this.globalReplacementState.target, left + NESTED_LABEL_INDENT, top + ENTRY_LABEL_Y, Math.max(40, labelRight - left - 8));

			this.chooseButton.setX(buttonX);
			this.chooseButton.setY(top + ENTRY_WIDGET_Y);
			this.chooseButton.render(guiGraphics, mouseX, mouseY, partialTick);
			if(this.chooseButton.isMouseOver(mouseX, mouseY))
				SubWildConfigScreen.this.queueTooltip(Component.literal("Open the cave selection screen and choose the SubWild cave type used for global replacement."), this.chooseButton.getX(), this.chooseButton.getY(), this.chooseButton.getWidth());
		}

		public List<? extends GuiEventListener> children()
		{
			return List.of(this.chooseButton);
		}

		@Override
		public boolean mouseClicked(double mouseX, double mouseY, int button)
		{
			return this.chooseButton.mouseClicked(mouseX, mouseY, button);
		}

		@Override
		public boolean mouseReleased(double mouseX, double mouseY, int button)
		{
			return this.chooseButton.mouseReleased(mouseX, mouseY, button);
		}

		@Override
		public Component getNarration()
		{
			return GLOBAL_REPLACE_CHOOSER;
		}
	}

	private final class GlobalReplacementDeepToggleEntry extends ConfigListEntry
	{
		private final Button toggleButton;

		private GlobalReplacementDeepToggleEntry()
		{
			this.toggleButton = Button.builder(this.message(), button ->
			{
				SubWildConfigScreen.this.globalReplacementState.affectDeepTuffCaves = !SubWildConfigScreen.this.globalReplacementState.affectDeepTuffCaves;
				button.setMessage(this.message());
			}).bounds(0, 0, 150, 20).build();
		}

		private Component message()
		{
			return SubWildConfigScreen.this.globalReplacementState.affectDeepTuffCaves ? VALUE_ON : VALUE_OFF;
		}

		@Override
		public void render(GuiGraphics guiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovered, float partialTick)
		{
			int buttonWidth = 150;
			int buttonX = left + width - buttonWidth - 6;
			int labelRight = buttonX - 8;
			guiGraphics.drawString(SubWildConfigScreen.this.font, GLOBAL_REPLACE_DEEP_TITLE, left + NESTED_LABEL_INDENT, top + ENTRY_LABEL_Y, 0xA0A0A0, false);

			this.toggleButton.setX(buttonX);
			this.toggleButton.setY(top + ENTRY_WIDGET_Y);
			this.toggleButton.setMessage(this.message());
			this.toggleButton.render(guiGraphics, mouseX, mouseY, partialTick);

			if(this.toggleButton.isMouseOver(mouseX, mouseY) || mouseX >= left + 4 && mouseX < labelRight && mouseY >= top && mouseY < top + 20)
				SubWildConfigScreen.this.queueTooltip(Component.literal("Extends the selected replacement cave type through all lower cave layers down to bedrock."), this.toggleButton.getX(), this.toggleButton.getY(), this.toggleButton.getWidth());
		}

		public List<? extends GuiEventListener> children()
		{
			return List.of(this.toggleButton);
		}

		@Override
		public boolean mouseClicked(double mouseX, double mouseY, int button)
		{
			return this.toggleButton.mouseClicked(mouseX, mouseY, button);
		}

		@Override
		public boolean mouseReleased(double mouseX, double mouseY, int button)
		{
			return this.toggleButton.mouseReleased(mouseX, mouseY, button);
		}

		@Override
		public Component getNarration()
		{
			return GLOBAL_REPLACE_DEEP_TITLE;
		}
	}

	private final class FeatureToggleEntry extends ConfigListEntry
	{
		private final FeatureToggleState state;
		private final Button toggleButton;

		private FeatureToggleEntry(FeatureToggleState state)
		{
			this.state = state;
			this.toggleButton = Button.builder(this.state.message(), button ->
			{
				this.state.toggle();
				button.setMessage(this.state.message());
			}).bounds(0, 0, 120, 20).build();
		}

		@Override
		public void render(GuiGraphics guiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovered, float partialTick)
		{
			int buttonWidth = Math.min(140, Math.max(96, SubWildConfigScreen.this.widgetWidth));
			int buttonX = left + width - buttonWidth - 6;
			int labelRight = buttonX - 8;
			String text = this.state.label().getString();
			String trimmed = SubWildConfigScreen.this.font.plainSubstrByWidth(text, Math.max(40, labelRight - left));
			if(trimmed.length() < text.length())
				trimmed = trimmed + "...";
			guiGraphics.drawString(SubWildConfigScreen.this.font, trimmed, left + 4, top + ENTRY_LABEL_Y, 0xFFFFFF, false);

			this.toggleButton.setX(buttonX);
			this.toggleButton.setY(top + ENTRY_WIDGET_Y);
			this.toggleButton.setWidth(buttonWidth);
			this.toggleButton.setMessage(this.state.message());
			this.toggleButton.render(guiGraphics, mouseX, mouseY, partialTick);

			if(this.toggleButton.isMouseOver(mouseX, mouseY) || mouseX >= left + 4 && mouseX < labelRight && mouseY >= top && mouseY < top + 20)
				SubWildConfigScreen.this.queueTooltip(this.state.description(), this.toggleButton.getX(), this.toggleButton.getY(), this.toggleButton.getWidth());
		}

		public List<? extends GuiEventListener> children()
		{
			return List.of(this.toggleButton);
		}

		@Override
		public boolean mouseClicked(double mouseX, double mouseY, int button)
		{
			return this.toggleButton.mouseClicked(mouseX, mouseY, button);
		}

		@Override
		public boolean mouseReleased(double mouseX, double mouseY, int button)
		{
			return this.toggleButton.mouseReleased(mouseX, mouseY, button);
		}

		@Override
		public Component getNarration()
		{
			return this.state.label();
		}
	}

	private final class CaveTypeEntry extends ConfigListEntry
	{
		private final CaveTypeState state;
		private final Button modeButton;

		private CaveTypeEntry(CaveTypeState state)
		{
			this.state = state;
			this.modeButton = Button.builder(SubWildConfigScreen.this.caveModeLabel(state.mode), button ->
			{
				this.state.mode = SubWildConfigScreen.this.nextMode(this.state.mode);
				SubWildConfigScreen.this.rebuildPageWidgets();
			}).bounds(0, 0, 150, 20).build();
		}

		@Override
		public void render(GuiGraphics guiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovered, float partialTick)
		{
			int buttonWidth = 150;
			int buttonX = left + width - buttonWidth - 6;
			int labelRight = buttonX - 8;
			String text = this.state.label;
			String trimmed = SubWildConfigScreen.this.font.plainSubstrByWidth(text, Math.max(40, labelRight - left));
			if(trimmed.length() < text.length())
				trimmed = trimmed + "...";
			guiGraphics.drawString(SubWildConfigScreen.this.font, trimmed, left + 4, top + ENTRY_LABEL_Y, SubWildConfigScreen.this.caveTypeColor(this.state.id), false);

			this.modeButton.setX(buttonX);
			this.modeButton.setY(top + ENTRY_WIDGET_Y);
			this.modeButton.setMessage(SubWildConfigScreen.this.caveModeLabel(this.state.mode));
			this.modeButton.render(guiGraphics, mouseX, mouseY, partialTick);

			if(this.modeButton.isMouseOver(mouseX, mouseY) || mouseX >= left + 4 && mouseX < labelRight && mouseY >= top && mouseY < top + 20)
				SubWildConfigScreen.this.queueTooltip(Component.literal(this.state.description), this.modeButton.getX(), this.modeButton.getY(), this.modeButton.getWidth());
		}

		public List<? extends GuiEventListener> children()
		{
			return List.of(this.modeButton);
		}

		@Override
		public boolean mouseClicked(double mouseX, double mouseY, int button)
		{
			return this.modeButton.mouseClicked(mouseX, mouseY, button);
		}

		@Override
		public boolean mouseReleased(double mouseX, double mouseY, int button)
		{
			return this.modeButton.mouseReleased(mouseX, mouseY, button);
		}

		@Override
		public Component getNarration()
		{
			return Component.literal(this.state.label);
		}
	}

	private final class CaveTypeReplacementEntry extends ConfigListEntry
	{
		private static final Component CHOOSE_REPLACEMENT = Component.literal("Choose Replacement Cave");

		private final CaveTypeState state;
		private final Button chooseButton;

		private CaveTypeReplacementEntry(CaveTypeState state)
		{
			this.state = state;
			this.chooseButton = Button.builder(CHOOSE_REPLACEMENT, button ->
				SubWildConfigScreen.this.openCaveTypeSelection("Select Replacement Cave", this.state.replacementTarget, selected ->
					this.state.replacementTarget = selected)).bounds(0, 0, 150, 20).build();
		}

		@Override
		public void render(GuiGraphics guiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovered, float partialTick)
		{
			int buttonWidth = 150;
			int buttonX = left + width - buttonWidth - 6;
			int labelRight = buttonX - 8;
			SubWildConfigScreen.this.drawCaveTypeReference(guiGraphics, SubWildConfigScreen.this.font, "Replacement: ",
				this.state.replacementTarget, left + NESTED_LABEL_INDENT, top + ENTRY_LABEL_Y, Math.max(40, labelRight - left - 8));

			this.chooseButton.setX(buttonX);
			this.chooseButton.setY(top + ENTRY_WIDGET_Y);
			this.chooseButton.render(guiGraphics, mouseX, mouseY, partialTick);
			if(this.chooseButton.isMouseOver(mouseX, mouseY))
				SubWildConfigScreen.this.queueTooltip(Component.literal("Open the cave selection screen and choose which SubWild cave type should replace this one."), this.chooseButton.getX(), this.chooseButton.getY(), this.chooseButton.getWidth());
		}

		public List<? extends GuiEventListener> children()
		{
			return List.of(this.chooseButton);
		}

		@Override
		public boolean mouseClicked(double mouseX, double mouseY, int button)
		{
			return this.chooseButton.mouseClicked(mouseX, mouseY, button);
		}

		@Override
		public boolean mouseReleased(double mouseX, double mouseY, int button)
		{
			return this.chooseButton.mouseReleased(mouseX, mouseY, button);
		}

		@Override
		public Component getNarration()
		{
			return CHOOSE_REPLACEMENT;
		}
	}

	private final class CaveTypeDeepToggleEntry extends ConfigListEntry
	{
		private final CaveTypeState state;
		private final Button toggleButton;

		private CaveTypeDeepToggleEntry(CaveTypeState state)
		{
			this.state = state;
			this.toggleButton = Button.builder(this.message(), button ->
			{
				this.state.affectDeepTuffCaves = !this.state.affectDeepTuffCaves;
				button.setMessage(this.message());
			}).bounds(0, 0, 150, 20).build();
		}

		private Component message()
		{
			return this.state.affectDeepTuffCaves ? VALUE_ON : VALUE_OFF;
		}

		@Override
		public void render(GuiGraphics guiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovered, float partialTick)
		{
			int buttonWidth = 150;
			int buttonX = left + width - buttonWidth - 6;
			int labelRight = buttonX - 8;
			guiGraphics.drawString(SubWildConfigScreen.this.font, GLOBAL_REPLACE_DEEP_TITLE, left + NESTED_LABEL_INDENT, top + ENTRY_LABEL_Y, 0xA0A0A0, false);

			this.toggleButton.setX(buttonX);
			this.toggleButton.setY(top + ENTRY_WIDGET_Y);
			this.toggleButton.setMessage(this.message());
			this.toggleButton.render(guiGraphics, mouseX, mouseY, partialTick);

			if(this.toggleButton.isMouseOver(mouseX, mouseY) || mouseX >= left + NESTED_LABEL_INDENT && mouseX < labelRight && mouseY >= top && mouseY < top + 20)
				SubWildConfigScreen.this.queueTooltip(Component.literal("Extends this cave type through all lower cave layers down to bedrock."), this.toggleButton.getX(), this.toggleButton.getY(), this.toggleButton.getWidth());
		}

		public List<? extends GuiEventListener> children()
		{
			return List.of(this.toggleButton);
		}

		@Override
		public boolean mouseClicked(double mouseX, double mouseY, int button)
		{
			return this.toggleButton.mouseClicked(mouseX, mouseY, button);
		}

		@Override
		public boolean mouseReleased(double mouseX, double mouseY, int button)
		{
			return this.toggleButton.mouseReleased(mouseX, mouseY, button);
		}

		@Override
		public Component getNarration()
		{
			return GLOBAL_REPLACE_DEEP_TITLE;
		}
	}

	private final class CaveTypeSelectionScreen extends Screen
	{
		private final Screen previousScreen;
		private final Consumer<ResourceLocation> onSelected;
		private final ResourceLocation currentSelection;
		private EditBox searchBox;
		private CaveChoiceList choiceList;
		private String searchQuery = "";

		private CaveTypeSelectionScreen(Screen previousScreen, Component title, ResourceLocation currentSelection, Consumer<ResourceLocation> onSelected)
		{
			super(title);
			this.previousScreen = previousScreen;
			this.currentSelection = currentSelection;
			this.onSelected = onSelected;
		}

		@Override
		protected void init()
		{
			int searchWidth = Math.min(220, this.width - 40);
			this.searchBox = this.addRenderableWidget(new EditBox(this.font, (this.width - searchWidth) / 2, 32, searchWidth, 18, Component.literal("Search SubWild Caves")));
			this.searchBox.setHint(Component.literal("Search SubWild caves..."));
			this.searchBox.setMaxLength(80);
			this.searchBox.setValue(this.searchQuery);
			this.searchBox.setResponder(value ->
			{
				if(value.equals(this.searchQuery))
					return;
				this.searchQuery = value;
				if(this.choiceList != null)
					this.choiceList.applyFilter(this.searchQuery);
			});
			this.choiceList = this.addRenderableWidget(new CaveChoiceList(this.minecraft, this.width, 56, this.height - 52));
			this.addRenderableWidget(Button.builder(Component.translatable("gui.cancel"), button -> this.minecraft.setScreen(this.previousScreen))
				.bounds((this.width - 120) / 2, this.height - 28, 120, 20).build());
		}

		@Override
		public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick)
		{
			this.renderBackground(guiGraphics);
			super.render(guiGraphics, mouseX, mouseY, partialTick);
			guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 16, 0xFFFFFF);
		}

		@Override
		public void onClose()
		{
			this.minecraft.setScreen(this.previousScreen);
		}

		private final class CaveChoiceList extends ObjectSelectionList<CaveChoiceEntry>
		{
			private CaveChoiceList(Minecraft minecraft, int width, int top, int bottom)
			{
				super(minecraft, width, CaveTypeSelectionScreen.this.height, top, bottom, 24);
				this.applyFilter(CaveTypeSelectionScreen.this.searchQuery);
			}

			private void applyFilter(String query)
			{
				this.clearEntries();
				for(SubWildConfig.CaveTypeChoice choice : SubWildConfigScreen.this.caveTypeChoices)
					if(matchesQuery(query, choice.label(), choice.id().toString()))
						this.addEntry(new CaveChoiceEntry(choice));
				this.setScrollAmount(0.0d);
				if(!this.children().isEmpty())
					this.centerScrollOn(this.children().get(0));
			}

			@Override
			public int getRowWidth()
			{
				return Math.min(300, CaveTypeSelectionScreen.this.width - 60);
			}

			@Override
			protected int getScrollbarPosition()
			{
				return this.getRowRight() + 8;
			}
		}

		private final class CaveChoiceEntry extends ObjectSelectionList.Entry<CaveChoiceEntry>
		{
			private final SubWildConfig.CaveTypeChoice choice;

			private CaveChoiceEntry(SubWildConfig.CaveTypeChoice choice)
			{
				this.choice = choice;
			}

			@Override
			public void render(GuiGraphics guiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovered, float partialTick)
			{
				boolean selected = this.choice.id().equals(CaveTypeSelectionScreen.this.currentSelection);
				int prefixColor = selected ? 0x55FF55 : 0xA0A0A0;
				String prefix = selected ? "> " : "";
				guiGraphics.drawString(CaveTypeSelectionScreen.this.font, prefix, left + 4, top + 6, prefixColor, false);
				guiGraphics.drawString(CaveTypeSelectionScreen.this.font, this.choice.label(), left + 4 + CaveTypeSelectionScreen.this.font.width(prefix), top + 6,
					SubWildConfigScreen.this.caveTypeColor(this.choice.id()), false);
			}

			@Override
			public boolean mouseClicked(double mouseX, double mouseY, int button)
			{
				if(button != 0)
					return false;
				CaveTypeSelectionScreen.this.onSelected.accept(this.choice.id());
				CaveTypeSelectionScreen.this.minecraft.setScreen(CaveTypeSelectionScreen.this.previousScreen);
				return true;
			}

			public List<? extends GuiEventListener> children()
			{
				return List.of();
			}

			@Override
			public Component getNarration()
			{
				return Component.literal(this.choice.label());
			}
		}
	}

	private static final class GlobalReplacementState
	{
		private boolean enabled;
		private boolean affectDeepTuffCaves;
		private ResourceLocation target;

		private GlobalReplacementState(boolean enabled, boolean affectDeepTuffCaves, ResourceLocation target)
		{
			this.enabled = enabled;
			this.affectDeepTuffCaves = affectDeepTuffCaves;
			this.target = target;
		}
	}

	private static final class CaveTypeState
	{
		private final ResourceLocation id;
		private final String label;
		private final String description;
		private SubWildConfig.CaveTypeMode mode;
		private boolean affectDeepTuffCaves;
		private ResourceLocation replacementTarget;

		private CaveTypeState(ResourceLocation id, String label, String description, SubWildConfig.CaveTypeMode mode, boolean affectDeepTuffCaves, ResourceLocation replacementTarget)
		{
			this.id = id;
			this.label = label;
			this.description = description;
			this.mode = mode;
			this.affectDeepTuffCaves = affectDeepTuffCaves;
			this.replacementTarget = replacementTarget;
		}
	}

	private record FeatureSection(String title, List<FeatureToggleState> options) {}

	private static final class FeatureToggleState
	{
		private final Component label;
		private final Component description;
		private final Consumer<Boolean> saver;
		private boolean enabled;

		private FeatureToggleState(Component label, Component description, boolean enabled, Consumer<Boolean> saver)
		{
			this.label = label;
			this.description = description;
			this.enabled = enabled;
			this.saver = saver;
		}

		private void toggle()
		{
			this.enabled = !this.enabled;
		}

		private Component message()
		{
			return this.enabled ? VALUE_ON : VALUE_OFF;
		}

		private void save()
		{
			this.saver.accept(this.enabled);
		}

		private Component label()
		{
			return this.label;
		}

		private Component description()
		{
			return this.description;
		}
	}
}
